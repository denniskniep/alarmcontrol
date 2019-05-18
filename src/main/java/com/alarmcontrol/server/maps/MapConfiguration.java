package com.alarmcontrol.server.maps;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties({GraphhopperRoutingProperties.class, GeocodingProperties.class})
public class MapConfiguration {

  @Bean("mapRestTemplate")
  public RestTemplate mapRestTemplate(RestTemplateBuilder builder){
    RestTemplate restTemplate = builder.build();
    return restTemplate;
  }
}
