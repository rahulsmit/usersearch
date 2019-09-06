package com.rahul.usersearch.repository;

import static org.mockito.Mockito.times;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahul.usersearch.model.user.User;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class ElasticSearchRepositoryTest {

  @Mock
  private RestHighLevelClient restHighLevelClient;

  @Mock
  private ObjectMapper objectMapper;

  @Mock
  private BulkProcessor bulkProcessor;

  private ElasticSearchRepository repositoryUnderTest;

  List<User> usersearchList;

  /**
   * Simple utility method to load mock data
   * @return
   */
  private List<User> fetchMockProducts(){
    ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(
        "mock_data/usersearch_data.json")) {
      return objectMapper.readValue(inputStream, new TypeReference<List<User>>() { });
    } catch (IOException ex) {
      throw new UncheckedIOException(ex);
    }
  }

  @Before
  public void setUp() {
    repositoryUnderTest = new ElasticSearchRepository(restHighLevelClient, objectMapper, bulkProcessor);

    ReflectionTestUtils.setField(repositoryUnderTest, "indexName", "test01");
    ReflectionTestUtils.setField(repositoryUnderTest, "bulkProcessor", bulkProcessor);

    usersearchList = fetchMockProducts();
  }

  @Test
  public void givenListOfProducts_bulkProcessorAddIsCalledForEach() throws JsonProcessingException {
    Mockito.when(objectMapper.writeValueAsBytes(Mockito.any())).thenReturn(new byte[]{});
      repositoryUnderTest.performBulkLoad(usersearchList);
      Mockito.verify(bulkProcessor, times(10)).add(Mockito.any(IndexRequest.class));
  }

  @Test
  public void whenCloseCalled_bulkProcessorCloseIsCalled() throws InterruptedException {
    repositoryUnderTest.closeBulkProcessor();
    Mockito.verify(bulkProcessor, times(1)).awaitClose(30L, TimeUnit.SECONDS);
  }
}