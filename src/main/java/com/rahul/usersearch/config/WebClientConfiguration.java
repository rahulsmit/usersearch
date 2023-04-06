package com.rahul.usersearch.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestClientBuilder;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@Slf4j
public class WebClientConfiguration {

    @Value("${client.connection-timeout:500000}")
    private int connectionTimeout;

    @Value("${client.read-timeout:600000}")
    private int readTimeout;

    @Value("${http.client.ssl.trust-store}")
    Resource resourceFile;

    @Value("${http.client.ssl.trust-store-password}")
    private String keyStorePassword;

    @Value("${es.host}")
    private String esHost;

    @Bean("simpleRestTemplate")
    RestTemplate restTemplate() {
        return new RestTemplate(getClientHttpRequestFactory());
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        int connectionTimeout = 60000; // milliseconds
        int socketTimeout = 50000; // milliseconds
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(connectionTimeout)
                .setConnectionRequestTimeout(connectionTimeout)
                .setSocketTimeout(socketTimeout)
                .build();
        CloseableHttpClient client = HttpClientBuilder
                .create()
                .setDefaultRequestConfig(config)
                .build();
        return new HttpComponentsClientHttpRequestFactory(client);
    }

    @Qualifier("common")
    @Bean(destroyMethod = "close")
    RestHighLevelClient restHighLevelClientDefault() {

        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("rk", "Oracle@123"));

        RestClientBuilder restClientBuilder = RestClient.builder(
                        new HttpHost(esHost, 9200, "https"))
                .setRequestConfigCallback(
                        requestConfigBuilder -> {
                            requestConfigBuilder.setConnectTimeout(5 * 60000);
                            requestConfigBuilder.setSocketTimeout(80 * 1000);
                            requestConfigBuilder.setConnectionRequestTimeout(5 * 60000);
                            return requestConfigBuilder;
                        })
                .setHttpClientConfigCallback(
                        httpClientBuilder -> {
                            //  httpClientBuilder.setSSLContext(esSslContext);
                            httpClientBuilder.setMaxConnPerRoute(100);
                            httpClientBuilder.setMaxConnTotal(100);
                            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                            httpClientBuilder.setSSLHostnameVerifier(
                                    (hostname, session) -> {
                                        log.warn("Skipping hostname verification for {}", hostname);
                                        return true;
                                    });
                            return httpClientBuilder;
                        });
        return new RestHighLevelClient(restClientBuilder);
    }

    @Bean
    ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

}
