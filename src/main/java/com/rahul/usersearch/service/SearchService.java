package com.rahul.usersearch.service;

import com.rahul.usersearch.model.UserListPage;
import com.rahul.usersearch.model.user.User;
import com.rahul.usersearch.repository.OpenSearchRepository;
import com.rahul.usersearch.utils.ElasticDSLBuilder;
import com.rahul.usersearch.utils.ElasticResponseProcessor;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import org.opensearch.action.search.SearchResponse;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

  public static int PAGE_SIZE = 10;

  private final OpenSearchRepository openSearchRepository;
  private final ElasticResponseProcessor elasticResponseProcessor;

  public static String[] indexList = new String [] {"cicqueuehistorysummary-000001",
          "cicqueuehistorysummary-000002",
    "cicqueuehistorysummary-000003",
    "cicqueuehistorysummary-000004",
    "cicqueuehistorysummary-000005",
    "cicqueuehistorysummary-000006",
    "cicqueuehistorysummary-000007",
    "cicqueuehistorysummary-000008",
    "cicqueuehistorysummary-000009",
    "cicqueuehistorysummary-000010",
    "cicqueuehistorysummary-000011",
    "cicqueuehistorysummary-000012",
    "cicqueuehistorysummary-000013"};


  public SearchService(OpenSearchRepository openSearchRepository,
                       ElasticResponseProcessor elasticResponseProcessor) {
    this.openSearchRepository = openSearchRepository;
    this.elasticResponseProcessor = elasticResponseProcessor;
  }

  public UserListPage doSearch() throws IOException {

    //Get elastic search domain object
    SearchResponse searchResponse = openSearchRepository
        .executeSearchQuery(ElasticDSLBuilder.createDSLQuery());

    //Convert it to our business domain object i.e. User
    List<User> searchResults = elasticResponseProcessor
        .processElasticSearchHits(searchResponse.getHits(), User.class);

    return new UserListPage(searchResults, null);
  }

  public void doSearch2() throws IOException {
    int num = getRandomNumber(0, indexList.length);
    openSearchRepository.executeSearchQuery(ElasticDSLBuilder.fullDSL(), "usersearch*");
  }

  public int getRandomNumber(int min, int max) {
    Random random = new Random();
    return random.nextInt(max - min) + min;
  }

  public boolean testConnect() throws IOException {
    //return elasticSearchRepository.testConnection1() && elasticSearchRepository.testConnection2();

    return openSearchRepository.testConnection1();
  }


}
