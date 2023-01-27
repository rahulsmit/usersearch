package com.rahul.usersearch.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;

@Configuration
@Slf4j
public class WebClientConfiguration {

  @Value("${client.connection-timeout:10000}")
  private int connectionTimeout;

  @Value("${client.read-timeout:60000}")
  private int readTimeout;

  @Value("${http.client.ssl.trust-store}")
  Resource resourceFile;

  @Value("${http.client.ssl.trust-store-password}")
  private String keyStorePassword;

  @Value("${es.host}")
  private String esHost;

  @Bean("simpleRestTemplate")
  RestTemplate restTemplate(){
    return new RestTemplateBuilder()
            .setConnectTimeout(connectionTimeout)
            .setReadTimeout(readTimeout)
            .build();
  }

  @Bean(destroyMethod = "close")
  RestHighLevelClient restHighLevelClient() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
    KeyStore truststore = KeyStore.getInstance("jks");
    try (InputStream is = resourceFile.getInputStream()) {
      truststore.load(is, keyStorePassword.toCharArray());
    } catch (IOException e) {
      e.printStackTrace();
    } catch (CertificateException e) {
      e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    SSLContextBuilder sslBuilder = SSLContexts.custom()
            .loadTrustMaterial(truststore, null);
    final SSLContext esSslContext = sslBuilder.build();

    RestClientBuilder restClientBuilder = RestClient.builder(
                    new HttpHost(esHost, 9200, "https"))
            .setRequestConfigCallback(
            requestConfigBuilder -> {
              requestConfigBuilder.setSocketTimeout(80 * 1000);
              return requestConfigBuilder;
            })
            .setHttpClientConfigCallback(
                    httpClientBuilder -> {
                      httpClientBuilder.setSSLContext(esSslContext);
                      httpClientBuilder.setMaxConnPerRoute(100);
                      httpClientBuilder.setMaxConnTotal(100);
                      httpClientBuilder.setSSLHostnameVerifier(
                              (hostname, session) -> {
                                log.warn("Skipping hostname verification for {}", hostname);
                                return true;
                              });
                      return httpClientBuilder;
                    });
    return new RestHighLevelClient(restClientBuilder);
  }

}
