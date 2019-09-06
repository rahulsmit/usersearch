package com.rahul.usersearch.repository;

import com.rahul.usersearch.model.UserListPage;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Repository
public class SourceDataRepository {

  private final RestTemplate restTemplate;
  private final String sourceDataUrl;

  @Autowired
  public SourceDataRepository(RestTemplate restTemplate,
      @Value("${sourcedata.url}") String sourceDataUrl) {
    this.restTemplate = restTemplate;
    this.sourceDataUrl = sourceDataUrl;
  }

  public UserListPage fetchUsersForOnePage(int pageNum) {

    URI requestUrl = UriComponentsBuilder.newInstance()
        .scheme("https").host(sourceDataUrl)
        .path("api")
        .queryParam("page", pageNum)
        .queryParam("results", 30)
        .queryParam("seed", "abcdefgh")
        .build()
        .encode()
        .toUri();

    RequestEntity<Void> requestEntity = RequestEntity.get(requestUrl).build();

    return restTemplate.exchange(requestEntity, UserListPage.class).getBody();
  }

}
