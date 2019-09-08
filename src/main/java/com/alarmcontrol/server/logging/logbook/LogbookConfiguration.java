package com.alarmcontrol.server.logging.logbook;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.function.Predicate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.DefaultHttpLogFormatter;
import org.zalando.logbook.HttpLogFormatter;
import org.zalando.logbook.JsonHttpLogFormatter;
import org.zalando.logbook.RawHttpRequest;
import org.zalando.logbook.SplunkHttpLogFormatter;

@Configuration
@EnableConfigurationProperties({LogbookCustomProperties.class})
public class LogbookConfiguration {

  private final LogbookCustomProperties properties;

  public LogbookConfiguration(LogbookCustomProperties properties) {
    this.properties = properties;
  }

  @Bean
  public Predicate<RawHttpRequest> requestCondition() {
    return (rawHttpRequest) -> {
      LogbookFilterProperties filterProperties = properties.getFilterProperties(rawHttpRequest);
      return Conditions.isRelevant(rawHttpRequest, filterProperties.getInclude(), filterProperties.getExclude());
    };
  }

  @Bean
  @ConditionalOnProperty(
      name = {"logbook.format.style"},
      havingValue = "http"
  )
  public HttpLogFormatter httpFormatter() {
    return new ConditionalHttpLogFormatter(new DefaultHttpLogFormatter(), properties);
  }

/*
  @Bean
  @ConditionalOnProperty(
      name = {"logbook.format.style"},
      havingValue = "curl"
  )
  public HttpLogFormatter curlFormatter() {
    return new ConditionalHttpLogFormatter(new CurlHttpLogFormatter(), properties);
  }*/

  @Bean
  @ConditionalOnProperty(
      name = {"logbook.format.style"},
      havingValue = "splunk"
  )
  public HttpLogFormatter splunkHttpLogFormatter() {
    return new ConditionalHttpLogFormatter(new SplunkHttpLogFormatter(), properties);
  }

  @Bean
  @ConditionalOnProperty(
      name = {"logbook.format.style"},
      havingValue = "json"
  )
  public HttpLogFormatter jsonFormatter(final ObjectMapper mapper) {
    return new ConditionalHttpLogFormatter(new JsonHttpLogFormatter(mapper), properties);
  }
}
