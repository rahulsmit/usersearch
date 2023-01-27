package com.rahul.usersearch.controller;

import com.rahul.usersearch.TestHelper;
import com.rahul.usersearch.model.UserListPage;
import com.rahul.usersearch.model.ReindexResult;
import com.rahul.usersearch.model.SearchRequest;
import com.rahul.usersearch.model.user.User;
import com.rahul.usersearch.service.ReIndexService;
import com.rahul.usersearch.service.SearchService;
import java.io.IOException;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

  @Mock
  ReIndexService reIndexService;

  @Mock
  SearchService searchService;

  @InjectMocks
  UserSearchController usersearchControllerUnderTest;

  public ReindexResult reindexResult;

  @Before
  public void setUp() {
    reindexResult = new ReindexResult(100, 100);
  }

  @Test
  public void given_reindexServiceRunsSuccessully_expect_accepted() throws IOException, InterruptedException {
    Mockito.when(reIndexService.reindex()).thenReturn(reindexResult);
    ResponseEntity<ReindexResult> actual = usersearchControllerUnderTest.reindex();
    Assert.assertEquals(reindexResult, actual.getBody());
    Assert.assertEquals(HttpStatus.ACCEPTED, actual.getStatusCode());
  }

  @Test(expected = IllegalStateException.class)
  public void given_reindexServiceThrowsException_expect_ExceptionThrown() throws IOException, InterruptedException {
    Mockito.when(reIndexService.reindex()).thenThrow(new IllegalStateException());
    ResponseEntity<ReindexResult> actual = usersearchControllerUnderTest.reindex();
  }
}