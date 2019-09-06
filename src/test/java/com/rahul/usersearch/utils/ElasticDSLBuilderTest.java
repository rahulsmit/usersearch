package com.rahul.usersearch.utils;

import static org.junit.Assert.*;

import com.rahul.usersearch.model.SearchRequest;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ElasticDSLBuilderTest {

  ElasticDSLBuilder elasticDSLBuilder;
  SearchRequest searchRequest;

  @Before
  public void setUp() throws Exception {

    searchRequest = new SearchRequest();
    searchRequest.setSearch("samsung");
  }

  @Test
  public void given_validSearchRequest_returnValidElasticDSL(){
    elasticDSLBuilder = new ElasticDSLBuilder();

    SearchSourceBuilder searchSourceBuilder = elasticDSLBuilder.createDSLQuery(searchRequest);

    String dsl = searchSourceBuilder.toString();

    Assert.assertEquals(
        "{\"from\":0,\"size\":10,\"query\":{\"bool\":{\"must\":[{\"simple_query_string\":{\"query\":\"samsung\",\"flags\":-1,\"default_operator\":\"or\",\"analyze_wildcard\":false,\"auto_generate_synonyms_phrase_query\":true,\"fuzzy_prefix_length\":0,\"fuzzy_max_expansions\":50,\"fuzzy_transpositions\":true,\"boost\":1.0}}],\"filter\":[{\"range\":{\"dob.age\":{\"from\":0,\"to\":2147483647,\"include_lower\":true,\"include_upper\":true,\"boost\":1.0}}}],\"adjust_pure_negative\":true,\"boost\":1.0}}}",
        dsl);
  }

}