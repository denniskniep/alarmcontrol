package com.alarmcontrol.server.logging.logbook;

import static org.zalando.logbook.BodyFilters.defaultValue;
import static org.zalando.logbook.BodyFilters.truncate;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.BodyFilter;
import org.zalando.logbook.BodyFilters;
import org.zalando.logbook.CurlHttpLogFormatter;
import org.zalando.logbook.DefaultHttpLogFormatter;
import org.zalando.logbook.HttpLogFormatter;
import org.zalando.logbook.JsonHttpLogFormatter;
import org.zalando.logbook.RawHttpRequest;
import org.zalando.logbook.SplunkHttpLogFormatter;
import org.zalando.logbook.spring.LogbookProperties;

@Configuration
@EnableConfigurationProperties({LogbookCustomProperties.class})
public class LogbookConfiguration {

  private final LogbookProperties properties;
  private final LogbookCustomProperties customProperties;

  public LogbookConfiguration(LogbookProperties properties,
      LogbookCustomProperties customProperties) {
    this.properties = properties;
    this.customProperties = customProperties;
  }

  @Bean
  public Predicate<RawHttpRequest> requestCondition() {
    return (rawHttpRequest) -> {
      LogbookFilterProperties filterProperties = customProperties.getFilterProperties(rawHttpRequest);
      return Conditions.isRelevant(rawHttpRequest, filterProperties.getInclude(), filterProperties.getExclude());
    };
  }

  @Bean
  public BodyFilter bodyFilter(final LogbookProperties properties) {
    final LogbookProperties.Write write = properties.getWrite();
    final int maxBodySize = write.getMaxBodySize();

    if (maxBodySize < 0) {
      return BodyFilter.merge(defaultValue(), customBodyFilter());
    }

    BodyFilter defaultBodyFilter = BodyFilter.merge(truncate(maxBodySize), defaultValue());
    return BodyFilter.merge(defaultBodyFilter, customBodyFilter());
  }

  private BodyFilter customBodyFilter(){
    final Set<String> properties = new HashSet<>();
    properties.add("idToken");
    properties.add("refreshToken");
    properties.add("password");
    return BodyFilters.replaceJsonStringProperty(properties,"XXX");
  }

  @Bean
  @ConditionalOnProperty(
      name = {"logbook.format.style"},
      havingValue = "http"
  )
  public HttpLogFormatter httpFormatter() {
    return new ConditionalHttpLogFormatter(new DefaultHttpLogFormatter(), customProperties);
  }

  @Bean
  @ConditionalOnProperty(
      name = {"logbook.format.style"},
      havingValue = "curl"
  )
  public HttpLogFormatter curlFormatter() {
    return new ConditionalHttpLogFormatter(new CurlHttpLogFormatter(), customProperties);
  }

  @Bean
  @ConditionalOnProperty(
      name = {"logbook.format.style"},
      havingValue = "splunk"
  )
  public HttpLogFormatter splunkHttpLogFormatter() {
    return new ConditionalHttpLogFormatter(new SplunkHttpLogFormatter(), customProperties);
  }

  @Bean
  @ConditionalOnProperty(
      name = {"logbook.format.style"},
      havingValue = "json"
  )
  public HttpLogFormatter jsonFormatter(final ObjectMapper mapper) {
    return new ConditionalHttpLogFormatter(new JsonHttpLogFormatter(mapper), customProperties);
  }


  @Bean
  @ConditionalOnProperty(
      name = {"logbook.format.style"},
      havingValue = "jsonWithMessage"
  )
  public HttpLogFormatter jsonWithMessageFormatter(final ObjectMapper mapper) {
    return new ConditionalHttpLogFormatter(new JsonHttpLogFormatter(mapper), new DefaultHttpLogFormatter(),
        customProperties);
  }


  @Bean
  public Logger httpLogger(final ObjectMapper mapper) {
    Logger logger = LoggerFactory.getLogger(properties.getWrite().getCategory());
    return new HttpLogger(logger, mapper);
  }
}
