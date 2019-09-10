package com.alarmcontrol.server.maps;

import com.alarmcontrol.server.maps.graphhopper.routing.GraphhopperRoutingProperties;
import com.alarmcontrol.server.maps.graphhopper.routing.GraphhopperRoutingService;
import com.alarmcontrol.server.maps.mapbox.geocoding.MapboxGeocodingProperties;
import com.alarmcontrol.server.maps.mapbox.geocoding.MapboxGeocodingService;
import com.alarmcontrol.server.maps.nominatim.geocoding.NominatimGeocodingProperties;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.httpclient.LogbookHttpRequestInterceptor;
import org.zalando.logbook.httpclient.LogbookHttpResponseInterceptor;

@Configuration
@EnableConfigurationProperties({
    GraphhopperRoutingProperties.class,
    NominatimGeocodingProperties.class,
    MapboxGeocodingProperties.class})
public class MapConfiguration {

  @Bean("mapRestTemplate")
  public RestTemplate mapRestTemplate(RestTemplateBuilder builder, Logbook logbook){

    CloseableHttpClient client = HttpClientBuilder.create()
        .addInterceptorFirst(new LogbookHttpRequestInterceptor(logbook))
        .addInterceptorFirst(new LogbookHttpResponseInterceptor())
        .build();

    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
    requestFactory.setHttpClient(client);

    RestTemplate restTemplate = builder
        .requestFactory(() -> requestFactory)
        .build();

    return restTemplate;
  }

  @Bean
  public GeocodingService geocodingService(CachingRestService restService,
      MapboxGeocodingProperties geocodingProperties){
    return new MapboxGeocodingService(restService, geocodingProperties);
  }

  @Bean
  public RoutingService routingService(GraphhopperRoutingProperties graphhopperRoutingProperties,
      CachingRestService restService){
    return new GraphhopperRoutingService(graphhopperRoutingProperties, restService);
  }
}
