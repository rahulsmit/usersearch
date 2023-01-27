package com.rahul.usersearch.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

@Slf4j
@Configuration
public class ElasticSearchConfiguration {

  @Value("${es.host}")
  private String esHost;

  @Autowired
  public RestHighLevelClient restHighLevelClient;

  @Bean(destroyMethod = "close")
  @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
  public BulkProcessor bulkProcessor(){
    return BulkProcessor.builder(
        (request, bulkListener) ->
            restHighLevelClient.bulkAsync(request, RequestOptions.DEFAULT, bulkListener),
        new BulkProcessor.Listener() {
          @Override
          public void beforeBulk(long executionId, BulkRequest request) {
            int numberOfActions = request.numberOfActions();
            log.info("Executing bulk [{}] with {} requests", executionId, numberOfActions);
          }

          @Override
          public void afterBulk(
              long executionId, BulkRequest request, BulkResponse response) {
            if (response.hasFailures()) {
              log.warn("Bulk [{}] executed with failures", executionId);
            } else {
              log.debug(
                  "Bulk [{}] completed in {} milliseconds",
                  executionId,
                  response.getTook().getMillis());
            }
          }

          @Override
          public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
            log.error("Failed to execute bulk", failure);
          }
        }).setConcurrentRequests(300)
        .setBulkActions(10) //10000 records per bulk operation
        .setFlushInterval(TimeValue.timeValueSeconds(120L))
            .setBulkSize(new ByteSizeValue(2, ByteSizeUnit.MB))
            .setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueSeconds(5L), 5))
        .build();
  }

//    @Bean(destroyMethod = "close")
//    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
//    public BulkProcessor bulkProcessor2(){
//        return BulkProcessor.builder(
//                        (request) ->
//                                restHighLevelClient.bulk(request, RequestOptions.DEFAULT)
//                .setBulkActions(10) //10000 records per bulk operation
//                .setFlushInterval(TimeValue.timeValueSeconds(120L))
//                .setBulkSize(new ByteSizeValue(2, ByteSizeUnit.MB))
//                .setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueSeconds(5L), 5))
//                .build();
//    }

}
