package com.rahul.usersearch.repository;

import com.rahul.usersearch.model.Address;

import java.net.URI;

import com.rahul.usersearch.model.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Repository
@Slf4j
public class SourceDataRepository {

  private final RestTemplate restTemplate;
  private final String sourceDataUrl;

  @Autowired
  public SourceDataRepository(@Qualifier("simpleRestTemplate")RestTemplate restTemplate,
      @Value("${sourcedata.url}") String sourceDataUrl) {
    this.restTemplate = restTemplate;
    this.sourceDataUrl = sourceDataUrl;
  }

  public Address[] fetchAddress() {

    URI requestUrl = UriComponentsBuilder.newInstance()
        .scheme("https").host(sourceDataUrl)
        .path("api/address/random_address")
        .queryParam("size", 100)
        .build()
        .encode()
        .toUri();

    RequestEntity<Void> requestEntity = RequestEntity.get(requestUrl).build();

    return restTemplate.exchange(requestEntity, Address[].class).getBody();
  }

  public User[] fetchUser() {

    URI requestUrl = UriComponentsBuilder.newInstance()
            .scheme("https").host(sourceDataUrl)
            .path("api/v2/users")
            .queryParam("size", 100)
            .build()
            .encode()
            .toUri();

    RequestEntity<Void> requestEntity = RequestEntity.get(requestUrl).build();

    ResponseEntity<User[]> responseEntity = restTemplate.exchange(requestEntity, User[].class);
    log.info("Source data fetch response: {}", responseEntity.getStatusCode());
    if(responseEntity.hasBody()){
      return responseEntity.getBody();
    } else {
      throw new IllegalStateException("Cant fetch source data");
    }
  }

}
