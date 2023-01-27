package com.rahul.usersearch.service;

import static org.mockito.Mockito.times;

import com.rahul.usersearch.model.Info;
import com.rahul.usersearch.model.UserListPage;
import com.rahul.usersearch.model.user.User;
import com.rahul.usersearch.repository.ElasticSearchRepository;
import com.rahul.usersearch.repository.SourceDataRepository;
import com.rahul.usersearch.utils.ElasticMetadataUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class ReIndexServiceTest {

  @Mock
  private SourceDataRepository sourceDataRepository;

  @Mock
  private ElasticSearchRepository elasticSearchRepository;

  @Mock
  private ElasticMetadataUtil elasticMetaDataUtil;

  @InjectMocks
  ReIndexService reIndexServiceUnderTest;

  UserListPage mockUserListPage;
  Info info;

  @Spy
  private List<User> usersearchs = new ArrayList<>();

  @Before
  public void setUp() throws Exception {
    mockUserListPage = Mockito.mock(UserListPage.class);
    info = Mockito.mock(Info.class);
    Mockito.when(mockUserListPage.getInfo()).thenReturn(info);
    Mockito.when(mockUserListPage.getResults()).thenReturn(usersearchs);
  }


}