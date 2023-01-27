package com.rahul.usersearch.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahul.usersearch.model.Address;
import com.rahul.usersearch.model.user.User;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class ElasticSearchRepository {

  @Value("${es.index}")
  private String indexName;

  private final RestHighLevelClient restHighLevelClient;
  private final ObjectMapper objectMapper;
  private final BulkProcessor bulkProcessor;

  @Autowired
  public ElasticSearchRepository(
      RestHighLevelClient restHighLevelClient,
      ObjectMapper objectMapper,
      BulkProcessor bulkProcessor) {
    this.restHighLevelClient = restHighLevelClient;
    this.objectMapper = objectMapper;
    this.bulkProcessor = bulkProcessor;

  }

  /**
   * We are using Elastic Search's JAVA Api's BulkProcessor
   * RestHighLevelClient : This client is used to execute the BulkRequest and get BulkResponse
   * BulkProcessor.Listener This listener is called
   * before and after every BulkRequest execution or when a BulkRequest failed
   *
   * @param items
   * @throws JsonProcessingException
   */
  public void performAsyncBulkLoad(List<Address> addresses) throws IOException, InterruptedException {
    String uniqueIndexName = indexName + Instant.now().toEpochMilli();
    log.info("Bulk loading users : " + addresses.size());

    CreateIndexRequest request = new CreateIndexRequest(uniqueIndexName);
    request.settings(Settings.builder()
            .put("index.number_of_shards", 1)
            .put("index.number_of_replicas", 0)
    );

    CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);

    log.info("Created Unique Index : {} result {}", uniqueIndexName,  createIndexResponse.isAcknowledged());

      for (Address address : addresses) {
        try {
          bulkProcessor.add(
                  new IndexRequest(uniqueIndexName)
                          .type("_doc")
                          .id(address.uid)
                          .source(objectMapper.writeValueAsBytes(address), XContentType.JSON));
        } catch (JsonProcessingException e) {
          e.printStackTrace();
        }
    }
    log.info("Done adding to bulkProcessor: {}", bulkProcessor.toString());
  }

  public void performSyncBulkLoad(List<Address> addresses) throws IOException {
    String uniqueIndexName = indexName + Instant.now().toEpochMilli();
    log.info("Bulk loading users : " + addresses.size());

    CreateIndexRequest request = new CreateIndexRequest(uniqueIndexName);
    request.settings(Settings.builder()
            .put("index.number_of_shards", 1)
            .put("index.number_of_replicas", 0)
    );

    CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);

    log.info("Created Unique Index : {} result {}", uniqueIndexName,  createIndexResponse.isAcknowledged());


    BulkRequest bulkRequest = new BulkRequest();
    for (Address address : addresses) {
      bulkRequest.add(
              new IndexRequest(uniqueIndexName)
                      .type("_doc")
                      .id(address.uid)
                      .source(objectMapper.writeValueAsBytes(address), XContentType.JSON));
    }

    BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);

    log.info("Sync Bulk Response Has Failures? {}", bulkResponse.hasFailures());
  }

  public void closeBulkProcessor() {
    try {
      bulkProcessor.awaitClose(300L, TimeUnit.SECONDS);
    } catch (InterruptedException ex) {
      bulkProcessor.close();
    } finally {
      log.info("Closed Bulk loader connection!");
    }
  }

  /**
   * Search Dao Method - Vanilla elastic executeSearchQuery query executer
   */
  public SearchResponse executeSearchQuery(SearchSourceBuilder searchSourceBuilder)
      throws IOException {
    log.debug("elasticsearch query: {}", searchSourceBuilder.query().toString());
    SearchResponse response = restHighLevelClient.search(new SearchRequest(indexName)
        .source(searchSourceBuilder), RequestOptions.DEFAULT);
    log.info("elasticsearch response: {} hits", response.getHits().totalHits);

    return response;
  }
}
