package com.rahul.usersearch.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ElasticResponseProcessor {

  private final ObjectMapper objectMapper;

  @Autowired
  public ElasticResponseProcessor(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  private <T> T convertToProductInfo(String json, Class<T> clazz) {
    try {
      return objectMapper.readValue(json, clazz);
    } catch (IOException ex) {
      throw new IllegalStateException("Json Serialization Error.");
    }
  }

  public <T> List<T> processElasticSearchHits(SearchHits searchHits, Class<T> clazz) {
    return Arrays.stream(searchHits.getHits())
        .map(s -> convertToProductInfo(s.getSourceAsString(), clazz))
        .collect(Collectors.toList());
  }

}
