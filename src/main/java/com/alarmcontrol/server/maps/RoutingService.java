package com.alarmcontrol.server.maps;

import com.alarmcontrol.server.maps.CachingRestService.Request;
import com.alarmcontrol.server.maps.CachingRestService.Response;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class RoutingService {
  private Logger logger = LoggerFactory.getLogger(RoutingService.class);
  private GraphhopperProperties graphhopperProperties;
  private CachingRestService restService;

  public RoutingService(GraphhopperProperties graphhopperProperties,
      CachingRestService restService) {
    this.graphhopperProperties = graphhopperProperties;
    this.restService = restService;
  }

  public Object route(List<String> points){
    logger.info("Start routing for waypoints: {}", asString(points));
    Request geocodeRequest = createGeocodeRequest(points);
    Response response = restService.executeRequest(geocodeRequest);

    if (!response.isFromCache()) {
      logRemainingRequests(response.getResponse());
    }
    return response.getResponse().getBody();
  }

  private CachingRestService.Request createGeocodeRequest(List<String> points) {
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(graphhopperProperties.getUrl());
    for (String point : points) {
      builder.queryParam("point",point);
    }
    builder.queryParam("instructions",true);
    builder.queryParam("type","json");
    builder.queryParam("key",graphhopperProperties.getApikey());

    HttpHeaders headers = new HttpHeaders();
    headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
    HttpEntity<Object> entity = new HttpEntity<>(headers);

    URI uri = builder.build().toUri();

    return new CachingRestService.Request(asString(points),
        uri,
        HttpMethod.GET,
        entity);
  }

  private String asString(List<String> points) {
    return points.stream().collect(Collectors.joining("|"));
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