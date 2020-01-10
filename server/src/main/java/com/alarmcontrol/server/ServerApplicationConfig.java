package com.alarmcontrol.server;

import okhttp3.OkHttpClient;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.okhttp.GzipInterceptor;
import org.zalando.logbook.okhttp.LogbookInterceptor;

@Configuration
public class ServerApplicationConfig {

  @Bean()
  public RestTemplate restTemplate(RestTemplateBuilder builder, Logbook logbook){

    OkHttpClient client = new OkHttpClient.Builder()
        .addNetworkInterceptor(new LogbookInterceptor(logbook))
        .addNetworkInterceptor(new GzipInterceptor())
        .build();

    OkHttp3ClientHttpRequestFactory requestFactory = new OkHttp3ClientHttpRequestFactory(client);
    requestFactory.setConnectTimeout(5000);
    requestFactory.setReadTimeout(10000);

    RestTemplate restTemplate = builder
        .requestFactory(() -> requestFactory)
        .build();

    return restTemplate;
  }
}
