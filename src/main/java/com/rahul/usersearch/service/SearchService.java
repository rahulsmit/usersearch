package com.rahul.usersearch.service;

import com.rahul.usersearch.model.Info;
import com.rahul.usersearch.model.UserListPage;
import com.rahul.usersearch.model.SearchRequest;
import com.rahul.usersearch.model.user.User;
import com.rahul.usersearch.repository.ElasticSearchRepository;
import com.rahul.usersearch.utils.ElasticDSLBuilder;
import com.rahul.usersearch.utils.ElasticResponseProcessor;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "usersearch-search-cache")
public class SearchService {

  public static int PAGE_SIZE = 10;

  private final ElasticSearchRepository elasticSearchRepository;
  private final ElasticDSLBuilder elasticDSLBuilder;
  private final ElasticResponseProcessor elasticResponseProcessor;

  public SearchService(ElasticSearchRepository elasticSearchRepository,
      ElasticDSLBuilder elasticDSLBuilder,
      ElasticResponseProcessor elasticResponseProcessor) {
    this.elasticSearchRepository = elasticSearchRepository;
    this.elasticDSLBuilder = elasticDSLBuilder;
    this.elasticResponseProcessor = elasticResponseProcessor;
  }

  @Cacheable
  public UserListPage doSearch(SearchRequest searchRequest) throws IOException {

    //Get elastic search domain object
    SearchResponse searchResponse = elasticSearchRepository
        .executeSearchQuery(elasticDSLBuilder.createDSLQuery(searchRequest));

    //Convert it to our business domain object i.e. User
    List<User> searchResults = elasticResponseProcessor
        .processElasticSearchHits(searchResponse.getHits(), User.class);

    Info info = new Info("abcdefg",
        Math.toIntExact(searchResponse.getHits().getTotalHits()),
            Optional.of(searchRequest.getPageNumber()).orElse(1),
        searchResults.size(),
        "v1");

    return new UserListPage(searchResults, info);
  }


}
