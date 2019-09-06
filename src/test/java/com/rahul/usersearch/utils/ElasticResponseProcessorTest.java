package com.rahul.usersearch.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahul.usersearch.model.user.User;
import java.util.List;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ElasticResponseProcessorTest {

  ElasticResponseProcessor elasticResponseProcessor;

  SearchHits mockSearchHits;
  SearchHit mockSearchHit;

  @Before
  public void setUp() throws Exception {

    elasticResponseProcessor = new ElasticResponseProcessor(new ObjectMapper());

    mockSearchHits = Mockito.mock(SearchHits.class);
    mockSearchHit = Mockito.mock(SearchHit.class);

    Mockito.when(mockSearchHit.getSourceAsString()).thenReturn("{\n"
        + "    \"usersearchId\":\"1\",\n"
        + "    \"usersearchName\":\"User 1\",\n"
        + "    \"shortDescription\":\"Short Description 1\",\n"
        + "    \"longDescription\":\"Long Description 1\",\n"
        + "    \"price\":\"$1.00\",\n"
        + "    \"usersearchImage\":\"/images/image1.jpeg\",\n"
        + "    \"reviewRating\":2,\n"
        + "    \"reviewCount\":100,\n"
        + "    \"inStock\":true\n"
        + "  }");

    SearchHit[] searchHitsArray = new SearchHit[] {mockSearchHit};
    Mockito.when(mockSearchHits.getHits()).thenReturn(searchHitsArray);


  }

  @Test
  @Ignore
  public void given_searchHit_returnListOfProducts(){
    List<User> usersearchList = elasticResponseProcessor
        .processElasticSearchHits(mockSearchHits, User.class);
    Assert.assertEquals(1, usersearchList.size());
    Assert.assertEquals("User 1", usersearchList.get(0).getName());
  }
}