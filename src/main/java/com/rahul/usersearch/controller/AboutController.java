package com.rahul.usersearch.controller;

import com.rahul.usersearch.utils.ElasticMetadataUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This is a controller to just provide application metadata
 */
@RestController
@RequestMapping("about")
public class AboutController {

  @Value("${info.app.name}")
  private String appName;

  @Value("${info.app.name}")
  private String appDescription;

  @Value("${info.app.version}")
  private String appVersion;

  private final ElasticMetadataUtil elasticMetaDataUtil;

  @Autowired
  public AboutController(ElasticMetadataUtil elasticMetaDataUtil) {
    this.elasticMetaDataUtil = elasticMetaDataUtil;
  }


  @GetMapping(path = "", produces = "application/json")
  public ResponseEntity<Map<String, Object>> getAbout() throws IOException {
    Map<String, Object> about = new HashMap<>();
    about.put("name", appName);
    about.put("description", appDescription);
    about.put("version", appVersion);
    about.put("elastic", elasticMetaDataUtil.getElasticInfo());
    return ResponseEntity.ok(about);
  }

}
