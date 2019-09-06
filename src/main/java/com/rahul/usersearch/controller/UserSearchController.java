package com.rahul.usersearch.controller;

import com.rahul.usersearch.model.UserListPage;
import com.rahul.usersearch.model.ReindexResult;
import com.rahul.usersearch.model.SearchRequest;
import com.rahul.usersearch.service.ReIndexService;
import com.rahul.usersearch.service.SearchService;
import java.io.IOException;
import javax.validation.Valid;
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
  public ResponseEntity<ReindexResult> reindex() throws IOException {
    ReindexResult reindexResult = reIndexService.reindex();
    return ResponseEntity.accepted().body(reindexResult);
  }

  @GetMapping(path = "searches", produces = "application/json")
  public ResponseEntity<UserListPage> doSearch(@Valid SearchRequest searchRequest) throws IOException {
    log.info("Search Params : " + searchRequest.toString());
    UserListPage result = searchService.doSearch(searchRequest);
    return ResponseEntity.ok().body(result);
  }
}
