package com.rahul.usersearch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rahul.usersearch.model.ReindexResult;
import com.rahul.usersearch.model.UserListPage;
import com.rahul.usersearch.repository.ElasticSearchRepository;
import com.rahul.usersearch.repository.SourceDataRepository;
import com.rahul.usersearch.utils.ElasticMetadataUtil;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReIndexService {

  private final SourceDataRepository sourceDataRepository;
  private final ElasticSearchRepository elasticSearchRepository;
  private final ElasticMetadataUtil elasticMetaDataUtil;

  @Autowired
  public ReIndexService(
      SourceDataRepository sourceDataRepository,
      ElasticSearchRepository elasticSearchRepository,
      ElasticMetadataUtil elasticMetadataUtil) {
    this.sourceDataRepository = sourceDataRepository;
    this.elasticSearchRepository = elasticSearchRepository;
    this.elasticMetaDataUtil = elasticMetadataUtil;
  }

  /**
   * This method gets data from provided URL and then indexes them to es
   *
   * @throws JsonProcessingException
   */
  public ReindexResult reindex() throws IOException {
    int total = 0;
    for(int pageNum = 1; pageNum <= 10; pageNum++){
      UserListPage usersearchListPage = sourceDataRepository.fetchUsersForOnePage(pageNum);
      log.info("Crawled page : " + pageNum);
      // Use Bulk API to load data to elastic
      elasticSearchRepository.performBulkLoad(usersearchListPage.getResults());
      total+=usersearchListPage.getInfo().getResults();
    }
    // closeBulkProcessor
    elasticSearchRepository.closeBulkProcessor();

    // wrap results
    return new ReindexResult(elasticMetaDataUtil.getIndexDocumentCount(), total);
  }
}
