package com.rahul.usersearch.controller;

import com.rahul.usersearch.model.UserListPage;
import com.rahul.usersearch.model.ReindexResult;
import com.rahul.usersearch.model.SearchRequest;
import com.rahul.usersearch.service.ReIndexService;
import com.rahul.usersearch.service.SearchService;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("users/v1/")
public class UserSearchController {

  private final ReIndexService reIndexService;
  private final SearchService searchService;

  @Autowired
  public UserSearchController(ReIndexService reIndexService,
      SearchService searchService) {
    this.reIndexService = reIndexService;
    this.searchService = searchService;
  }

  @PostMapping(path = "reindex", produces = "application/json")
  public ResponseEntity<ReindexResult> reindex() throws IOException, InterruptedException {
    log.info("Start reindex");
    ReindexResult reindexResult = reIndexService.reindex();
    return ResponseEntity.accepted().body(reindexResult);
  }

  @GetMapping(path = "doSearch", produces = "application/json")
  public ResponseEntity doSearch2() {

      Runnable searchRunnable = () -> {
          try {
              searchService.doSearch2();
          } catch (IOException e) {
              log.error("Error in doSearch2", e);
          }
      };

      ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);
      executor.scheduleAtFixedRate(searchRunnable, 0, 2, TimeUnit.SECONDS);

    return ResponseEntity.ok().body("DONE");
  }

  @GetMapping(path = "test", produces = "application/json")
  public ResponseEntity<Boolean> test() throws IOException {
    Boolean result = searchService.testConnect();
    if(result){
      ResponseEntity.ok().body(result);
    }
    return ResponseEntity.status(500).body(result);
  }
}
