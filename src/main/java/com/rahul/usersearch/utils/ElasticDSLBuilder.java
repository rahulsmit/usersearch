package com.rahul.usersearch.utils;

import static com.rahul.usersearch.service.SearchService.PAGE_SIZE;

import com.rahul.usersearch.model.SearchRequest;
import java.util.Optional;
import org.elasticsearch.common.Strings;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

@Component
public class ElasticDSLBuilder {

  /**
   * This method converts domain search request to elastic dsl.
   * Also handles pagination for search results.
   */
  public SearchSourceBuilder createDSLQuery(SearchRequest searchRequest) {
    BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

    //filter check price range
    queryBuilder.filter().add(QueryBuilders
        .rangeQuery("dob.age")
        .from(Optional.ofNullable(searchRequest.getMinAge()).orElse(0))
        .to(Optional.ofNullable(searchRequest.getMaxAge()).orElse(Integer.MAX_VALUE)));

    // finally match executeSearchQuery term(s)
    if (!Strings.isNullOrEmpty(searchRequest.getSearch())) {
      queryBuilder.must(QueryBuilders.simpleQueryStringQuery(searchRequest.getSearch()));
    }

    return SearchSourceBuilder
        .searchSource().query(queryBuilder)
        .from((searchRequest.getPageNumber() - 1) * PAGE_SIZE)
        .size(PAGE_SIZE)
        .trackTotalHits(true);
  }

}
