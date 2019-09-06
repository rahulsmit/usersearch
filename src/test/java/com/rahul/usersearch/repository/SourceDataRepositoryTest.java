package com.rahul.usersearch.repository;

import com.rahul.usersearch.TestHelper;
import com.rahul.usersearch.model.Info;
import com.rahul.usersearch.model.UserListPage;
import java.net.URI;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RunWith(MockitoJUnitRunner.class)
public class SourceDataRepositoryTest {

  @Mock
  RestTemplate restTemplate;

  SourceDataRepository sourceDataRepositoryUnderTest;

  @Mock
  ResponseEntity<UserListPage> mockResponseEntity;

  @Before
  public void setUp() throws Exception {
    sourceDataRepositoryUnderTest = new SourceDataRepository(restTemplate, "randomuser.me");
  }

  @Test
  public void given_goodDataFromSource_expect_validResponseEntity(){

    UserListPage usersearchListPage = new UserListPage();
    usersearchListPage.setInfo(new Info("xyz", 1, 20, 10, "v1"));
    usersearchListPage.setResults(TestHelper.fetchMockUsers());

    URI requestUrl = UriComponentsBuilder.newInstance()
        .scheme("https").host("randomuser.me")
        .path("api")
        .queryParam("page", 1)
        .queryParam("results", 30)
        .queryParam("seed", "abcdefgh")
        .build()
        .encode()
        .toUri();
    RequestEntity<Void> requestEntity = RequestEntity.get(requestUrl).build();

    Mockito.when(mockResponseEntity.getBody()).thenReturn(usersearchListPage);
    Mockito.when(restTemplate.exchange(requestEntity
            , UserListPage.class)).thenReturn(mockResponseEntity);

    UserListPage actual = sourceDataRepositoryUnderTest.fetchUsersForOnePage(1);

    Assert.assertEquals(usersearchListPage, actual);

  }
}