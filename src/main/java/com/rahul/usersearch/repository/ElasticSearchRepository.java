package com.rahul.usersearch.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahul.usersearch.model.user.User;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
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
  public void performBulkLoad(List<User> items) throws JsonProcessingException {
    for (User user : items) {
      bulkProcessor.add(
          new IndexRequest(indexName)
              .id(user.getLogin().getUuid())
              .type("default")
              .source(this.objectMapper.writeValueAsBytes(user), XContentType.JSON));
    }
  }

  public void closeBulkProcessor() {
    try {
      bulkProcessor.awaitClose(30L, TimeUnit.SECONDS);
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
