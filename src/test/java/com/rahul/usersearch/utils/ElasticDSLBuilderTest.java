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

}