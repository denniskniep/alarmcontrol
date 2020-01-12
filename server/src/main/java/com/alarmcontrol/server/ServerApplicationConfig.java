package com.alarmcontrol.server;

import com.alarmcontrol.server.utils.DefaultRetryListenerSupport;
import okhttp3.OkHttpClient;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.okhttp.GzipInterceptor;
import org.zalando.logbook.okhttp.LogbookInterceptor;

@EnableRetry
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

  @Bean
  public RetryTemplate retryTemplate() {
    RetryTemplate retryTemplate = new RetryTemplate();

    FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
    fixedBackOffPolicy.setBackOffPeriod(2000l);
    retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

    SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
    retryPolicy.setMaxAttempts(3);
    retryTemplate.setRetryPolicy(retryPolicy);

    retryTemplate.registerListener(new DefaultRetryListenerSupport());
    return retryTemplate;
  }
}
