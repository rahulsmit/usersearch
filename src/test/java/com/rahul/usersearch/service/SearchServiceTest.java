package com.rahul.usersearch.service;

import static org.mockito.Mockito.times;

import com.rahul.usersearch.TestHelper;
import com.rahul.usersearch.model.UserListPage;
import com.rahul.usersearch.model.SearchRequest;
import com.rahul.usersearch.model.user.User;
import com.rahul.usersearch.repository.ElasticSearchRepository;
import com.rahul.usersearch.utils.ElasticDSLBuilder;
import com.rahul.usersearch.utils.ElasticResponseProcessor;
import java.io.IOException;
import java.util.List;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SearchServiceTest {

  @Mock
  ElasticSearchRepository elasticSearchRepository;

  @Mock
  ElasticDSLBuilder elasticDSLBuilder;

  @Mock
  ElasticResponseProcessor elasticResponseProcessor;

  @InjectMocks
  SearchService searchServiceUnderTest;

  SearchResponse mockSearchResponse;
  SearchSourceBuilder mockSearchSourceBuilder;
  SearchHits searchHits;

  List<User> mockProductList;

  @Before
  public void setUp() throws Exception {

    mockSearchSourceBuilder = Mockito.mock(SearchSourceBuilder.class);
    mockSearchResponse = Mockito.mock(SearchResponse.class);
    searchHits = Mockito.mock(SearchHits.class);

    mockProductList = TestHelper.fetchMockUsers();

    Mockito.when(elasticDSLBuilder.createDSLQuery(Mockito.any(SearchRequest.class))).thenReturn(mockSearchSourceBuilder);
    Mockito.when(elasticSearchRepository.executeSearchQuery(Mockito.any(SearchSourceBuilder.class))).thenReturn(mockSearchResponse);
    Mockito.when(mockSearchResponse.getHits()).thenReturn(searchHits);
    Mockito.when(searchHits.getTotalHits()).thenReturn(4L);

    Mockito.when(elasticResponseProcessor.processElasticSearchHits(searchHits, User.class)).thenReturn(mockProductList);
  }

  @Test
  public void given_executeSearchQuerySucceeds_expectProductListPageHasProducts() throws IOException {
    UserListPage usersearchListPageActual = searchServiceUnderTest.doSearch(new SearchRequest());
    Mockito.verify(elasticSearchRepository, times(1)).executeSearchQuery(mockSearchSourceBuilder);
    Assert.assertEquals(mockProductList, usersearchListPageActual.getResults());
    Assert.assertEquals(4, usersearchListPageActual.getInfo().getResults());
  }
}