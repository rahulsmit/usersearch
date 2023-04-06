package com.rahul.usersearch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rahul.usersearch.model.ReindexResult;
import com.rahul.usersearch.model.user.User;
import com.rahul.usersearch.repository.OpenSearchRepository;
import com.rahul.usersearch.repository.SourceDataRepository;
import com.rahul.usersearch.utils.ElasticMetadataUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReIndexService {

  private final SourceDataRepository sourceDataRepository;
  private final OpenSearchRepository openSearchRepository;
  private final ElasticMetadataUtil elasticMetaDataUtil;

  @Autowired
  public ReIndexService(
      SourceDataRepository sourceDataRepository,
      OpenSearchRepository openSearchRepository,
      ElasticMetadataUtil elasticMetadataUtil) {
    this.sourceDataRepository = sourceDataRepository;
    this.openSearchRepository = openSearchRepository;
    this.elasticMetaDataUtil = elasticMetadataUtil;
  }

  /**
   * This method gets data from provided URL and then indexes them to es
   *
   * @throws JsonProcessingException
   */
  public ReindexResult reindex() throws IOException, InterruptedException {

      for(int i=1; i < 101; i++){
          readAndLoad(i);
      }

    // wrap results
    return new ReindexResult(elasticMetaDataUtil.getIndexDocumentCount(), 40000);
  }

    private void readAndLoad(int batch) throws InterruptedException {
        Thread.sleep(TimeUnit.SECONDS.toMillis(2));

        List<User> userList = new ArrayList<>();
        for(int pageNum=0; pageNum < 400; pageNum++) {
            log.info("\n============================= Running batch {} Page {} ==================================", batch, pageNum);
            userList.clear();

            for (int j=0; j<5; j++){
                User[] users = sourceDataRepository.fetchUser();
                userList.addAll(Arrays.asList(users));
            }

            log.info("Crawled page from source {} of batch {} record size {}", pageNum, batch, userList.size());
            // Use Bulk API to load data to elastic
                try {
                    openSearchRepository.bulkIngestCluster(userList);
                } catch (IOException e) {
                    log.error("performSyncBulkLoad error : ", e);
                }
            log.info("\n============================= Finishing batch {} Page {} ==================================", batch, pageNum);
        }


    }
}
