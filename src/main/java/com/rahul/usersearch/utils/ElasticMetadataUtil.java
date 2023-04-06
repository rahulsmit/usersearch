package com.rahul.usersearch.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.action.admin.cluster.health.ClusterHealthRequest;
import org.opensearch.client.Request;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.Response;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * A helper component to provide info and stats
 * about elastic search index. It can be leveraged to
 * provide more info if needed.
 */
@Slf4j
@Component
public class ElasticMetadataUtil {

  @Value("${es.index}")
  private String indexName;


  private final RestHighLevelClient restHighLevelClient;
  private final ObjectMapper objectMapper;

  @Autowired
  public ElasticMetadataUtil(  @Qualifier("common") RestHighLevelClient restHighLevelClient,
      ObjectMapper objectMapper) {
    this.restHighLevelClient = restHighLevelClient;
    this.objectMapper = objectMapper;
  }

  /***
   * Get some info of cluster
   * @return
   * @throws IOException
   */
  public Map<String, String> getElasticInfo() throws IOException {
    Map<String, String> about = new HashMap<>();
    ClusterHealthRequest request = new ClusterHealthRequest();
    about.put("cluster", restHighLevelClient.cluster().health(request, RequestOptions.DEFAULT).getClusterName());
    about.put("indexes", restHighLevelClient.cluster().health(request, RequestOptions.DEFAULT).getIndices().get(indexName).toString());
    about.put("docCount", String.valueOf(getIndexDocumentCount()));
    return about;
  }

  /**
   * Get document count (not perfect but works as usersearch document is flat)
   * @return
   * @throws IOException
   */
  public int getIndexDocumentCount() throws IOException {
    RestClient lowLevelClient = restHighLevelClient.getLowLevelClient();

    Response refresh = lowLevelClient.performRequest(new Request("POST", indexName + "*/_refresh"));
    Response countResponse = lowLevelClient.performRequest(new Request("GET", indexName + "*/_count"));
    JsonNode jsonNode = objectMapper.readValue(countResponse.getEntity().getContent(), JsonNode.class);
    return jsonNode.findValue("count").asInt();
  }

}
