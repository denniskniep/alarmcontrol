package com.alarmcontrol.server;

import java.time.Duration;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.httpclient.LogbookHttpRequestInterceptor;
import org.zalando.logbook.httpclient.LogbookHttpResponseInterceptor;

@Configuration
public class ServerApplicationConfig {

  public static final int HTTP_CONNECT_TIMEOUT_IN_MS = 5000;
  public static final int HTTP_READ_TIMEOUT_IN_MS = 10000;
  public static final int HTTP_CONNECTION_REQUEST_TIMEOUT_IN_MS = 5000;

  @Bean()
  public RestTemplate restTemplate(RestTemplateBuilder builder, Logbook logbook){

   /* CloseableHttpClient client = HttpClientBuilder.create()
        .addInterceptorFirst(new LogbookHttpRequestInterceptor(logbook))
        .addInterceptorLast(new LogbookHttpResponseInterceptor())
        .build();

    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
    requestFactory.setHttpClient(client);
    requestFactory.setConnectTimeout(HTTP_CONNECT_TIMEOUT_IN_MS);
    requestFactory.setReadTimeout(HTTP_READ_TIMEOUT_IN_MS);
    requestFactory.setConnectionRequestTimeout(HTTP_CONNECTION_REQUEST_TIMEOUT_IN_MS);*/

    RestTemplate restTemplate = builder
        //BufferClientHttpRequestFactory allows the response to be read more than one time
        //.requestFactory(() -> new BufferingClientHttpRequestFactory(requestFactory))
        .setConnectTimeout(Duration.ofMillis(HTTP_CONNECT_TIMEOUT_IN_MS))
        .setReadTimeout(Duration.ofMillis(HTTP_READ_TIMEOUT_IN_MS))
        .build();

    return restTemplate;
  }
}
