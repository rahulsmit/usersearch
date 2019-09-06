package com.rahul.usersearch.config;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebClientConfiguration {

  @Value("${client.connection-timeout:10000}")
  private int connectionTimeout;

  @Value("${client.read-timeout:60000}")
  private int readTimeout;

  @Bean
  RestTemplate restTemplate(){
    return new RestTemplateBuilder()
        .setConnectTimeout(connectionTimeout)
        .setReadTimeout(readTimeout)
        .build();
  }

}
