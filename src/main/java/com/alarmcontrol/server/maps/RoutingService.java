package com.alarmcontrol.server.maps;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class RoutingService {

  private Logger logger = LoggerFactory.getLogger(RoutingService.class);
  private RestTemplate restTemplate;
  private GraphhopperProperties graphhopperProperties;
  private ConcurrentHashMap<String, Object> routingCache;

  public RoutingService(@Qualifier("mapRestTemplate") RestTemplate restTemplate,
      GraphhopperProperties graphhopperProperties) {
    this.restTemplate = restTemplate;
    this.graphhopperProperties = graphhopperProperties;
    this.routingCache = new ConcurrentHashMap<>();
  }

  public Object route(List<String> points){
    String cacheKey = getCacheKey(points);
    logger.info("Start routing waypoints: {}", cacheKey);
    if(routingCache.containsKey(cacheKey)){
      logger.info("Using Route from Cache");
      return routingCache.get(cacheKey);
    }

    Object routingResult = executeRouting(points);
    logger.info("Routing finished");
    cacheRequest(cacheKey, routingResult);
    return routingResult;
  }

  private Object executeRouting(List<String> points) {
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(graphhopperProperties.getUrl());
    for (String point : points) {
      builder.queryParam("point",point);
    }
    builder.queryParam("instructions",true);
    builder.queryParam("type","json");
    builder.queryParam("key",graphhopperProperties.getApikey());

    HttpHeaders headers = new HttpHeaders();
    headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
    HttpEntity<String> entity = new HttpEntity<>(headers);

    URI uri = builder.build().toUri();
    ResponseEntity<Object> result = restTemplate.exchange(uri, HttpMethod.GET, entity, Object.class);

    logRemainingRequests(result);

    if(result.getStatusCode().isError()){
      logger.error("Error during routing request. StatusCode={}, Body={}", result.getStatusCodeValue(), result.getBody().toString());
    }else{
      logger.info("Routing request successful");
    }

    return result.getBody();
  }

  private String getCacheKey(List<String> points){
    return points.stream().collect(Collectors.joining("|"));
  }

  private void cacheRequest(String key, Object routingResult) {
    logger.info("Caching route");
    routingCache.put(key, routingResult);
  }

  private void logRemainingRequests(ResponseEntity<Object> result) {
    Optional<String> remaining = result
        .getHeaders()
        .get("X-RateLimit-Remaining")
        .stream()
        .findFirst();

    if(remaining.isPresent()){
      Integer remainingValue = null;
      try{
        remainingValue = Integer.parseInt(remaining.get());
      }catch (NumberFormatException e){
        logger.warn("Can not parse X-RateLimit-Remaining header from response");
      }

      if(remainingValue != null && remainingValue < 100){
        logger.warn("Only {} remaining routing requests", remainingValue);
      }else if(remainingValue != null){
        logger.info("{} remaining routing requests", remainingValue);
      }
    }
  }
}
