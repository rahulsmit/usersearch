package com.rahul.usersearch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rahul.usersearch.model.Address;
import com.rahul.usersearch.model.ReindexResult;
import com.rahul.usersearch.repository.ElasticSearchRepository;
import com.rahul.usersearch.repository.SourceDataRepository;
import com.rahul.usersearch.utils.ElasticMetadataUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
  public ReindexResult reindex() throws IOException, InterruptedException {

      for(int i=0; i < 50; i++){

          int finalI = i;
          CompletableFuture.runAsync(() -> {
              try {
                  readAndLoad(finalI);
              } catch (IOException e) {
                  log.error("readAndLoad error : ", e);
              }
          });
      }


      elasticSearchRepository.closeBulkProcessor();

    // wrap results
    return new ReindexResult(elasticMetaDataUtil.getIndexDocumentCount(), 40000);
  }

    private void readAndLoad(int batch) throws IOException {
        log.info("Running batch {}", batch);
        List<Address> addressList = new ArrayList<>();
        for(int pageNum=0; pageNum < 400; pageNum++) {
            addressList.clear();
            Address[] addresses = sourceDataRepository.fetchAddress();
            log.info("Crawled page {} of batch {} ", pageNum, batch);
            addressList.addAll(Arrays.asList(addresses));
          // Use Bulk API to load data to elastic
    //      elasticSearchRepository.performAsyncBulkLoad(addressList);
            elasticSearchRepository.performSyncBulkLoad(addressList);
            // closeBulkProcessor
          //Thread.sleep(TimeUnit.SECONDS.toMillis());
        }
    }
}
