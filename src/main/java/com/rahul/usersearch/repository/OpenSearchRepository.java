package com.rahul.usersearch.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahul.usersearch.model.user.User;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import com.rahul.usersearch.utils.ElasticDSLBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.opensearch.action.ActionListener;
import org.opensearch.action.DocWriteRequest;
import org.opensearch.action.admin.cluster.health.ClusterHealthRequest;
import org.opensearch.action.admin.cluster.health.ClusterHealthResponse;
import org.opensearch.action.bulk.BulkRequest;
import org.opensearch.action.bulk.BulkResponse;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.Request;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.Response;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.client.indices.CreateIndexRequest;
import org.opensearch.client.indices.CreateIndexResponse;
import org.opensearch.client.indices.GetIndexRequest;
import org.opensearch.common.settings.Settings;
import org.opensearch.common.xcontent.XContentType;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class OpenSearchRepository {

  @Value("${es.index}")
  private String indexName;

  @Qualifier("common")
  private final RestHighLevelClient defaultRestHighLevelClient;

  private final ObjectMapper objectMapper;

  @Autowired
  public OpenSearchRepository(
          @Qualifier("common") RestHighLevelClient defaultRestHighLevelClient,
          ObjectMapper objectMapper) {
    this.defaultRestHighLevelClient = defaultRestHighLevelClient;
    this.objectMapper = objectMapper;

  }

  public void bulkIngestCluster(List<User> users) throws IOException {
    log.info("-----> Bulk Sync rk_test_cluster");
    performASyncBulkLoad(users, defaultRestHighLevelClient);

  }

  public void performASyncBulkLoad(List<User> users, final RestHighLevelClient restHighLevelClient) throws IOException {
    String uniqueIndexName = indexName + Instant.now().toEpochMilli();
    log.info("Bulk loading {} users to elastic index: {} ", users.size(), indexName);

    GetIndexRequest getIndexRequest = new GetIndexRequest(uniqueIndexName);

    if(!restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT)) {
      CreateIndexRequest request = new CreateIndexRequest(uniqueIndexName);
      request.settings(Settings.builder()
              .put("index.number_of_shards", 1)
              .put("index.number_of_replicas", 0)
      );
      CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);

      log.info("Created Unique Index : {} result {}", uniqueIndexName, createIndexResponse.isAcknowledged());
//    }

      BulkRequest bulkRequest = new BulkRequest();
      for (User user : users) {
        bulkRequest.add(
                new IndexRequest(uniqueIndexName)
                        .opType(DocWriteRequest.OpType.INDEX)
                        .id(user.getUid())
                        .source(objectMapper.writeValueAsBytes(user), XContentType.JSON));
      }

      log.info("Bulk Request {}", bulkRequest.requests().size());
      restHighLevelClient.bulkAsync(bulkRequest, RequestOptions.DEFAULT, listener);
    }
  }

  ActionListener<BulkResponse> listener = new ActionListener<BulkResponse>() {
    @Override
    public void onResponse(BulkResponse bulkResponse) {
      log.info("Received Bulk response {}: Failures: {}", Arrays.stream(bulkResponse.getItems()).findFirst().get().getIndex(),  bulkResponse.hasFailures());
    }

    @Override
    public void onFailure(Exception e) {
      log.info("Exception Bulk response: ", e);
    }
  };

  /**
   * Search Dao Method - Vanilla elastic executeSearchQuery query executer
   */
  public SearchResponse executeSearchQuery(SearchSourceBuilder searchSourceBuilder)
      throws IOException {
    log.debug("elasticsearch query: {}", searchSourceBuilder.query().toString());

    SearchRequest searchRequest = new SearchRequest("usersearch*");
    searchRequest.requestCache(false);

    SearchResponse response = defaultRestHighLevelClient.search(searchRequest.source(searchSourceBuilder), RequestOptions.DEFAULT);
    log.info("elasticsearch response: {} hits", response.getHits().getHits().length);

    return response;
  }

  public void executeSearchQuery(String stringQuery, String indexName)
          throws IOException {
    log.debug("Querying index: {}", indexName);
    log.debug("elasticsearch dsl: {}", stringQuery);
    Request request = new Request("GET", indexName+"/_search?request_cache=false");
    request.setEntity(new NStringEntity(ElasticDSLBuilder.fullDSL(), ContentType.APPLICATION_JSON));
    Response response = defaultRestHighLevelClient.getLowLevelClient().performRequest(request);
    JsonNode jsonNode = objectMapper.readTree(response.getEntity().getContent());
    int statusCode = response.getStatusLine().getStatusCode();
    log.info("Response Code: {}", statusCode);
    if(statusCode == 200){
      log.warn("Querying Index {} Response Code: {} Response Took={} ms", indexName, statusCode, jsonNode.get("took"));
    }
  }

  public boolean testConnection1() {
    try {
      log.info("Client {}", defaultRestHighLevelClient.getLowLevelClient().getNodes().toString());
      ClusterHealthResponse clusterHealthResponse = defaultRestHighLevelClient.cluster().health(new ClusterHealthRequest(), RequestOptions.DEFAULT);
      log.info("ClusterHealthResponse: {}", clusterHealthResponse);
      return true;
    }catch (IOException ex){
      log.error("Cannot connect to cluster", ex);
      return false;
    }
  }
}
