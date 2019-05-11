package com.alarmcontrol.server.maps;

import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MapController {

  private RoutingService routingService;
  private GeocodingService geocodingService;
  private GeocodingReverseService geocodingReverseService;

  public MapController(RoutingService routingService, GeocodingService geocodingService,
      GeocodingReverseService geocodingReverseService) {
    this.routingService = routingService;
    this.geocodingService = geocodingService;
    this.geocodingReverseService = geocodingReverseService;
  }

  @GetMapping(value = "/map/route", produces = "application/json")
  public ResponseEntity<Object> route(@RequestParam(value="point") List<String> points){
    Object routingResult = routingService.route(points);

    //Necessary for the client library
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set("access-control-expose-headers", "X-RateLimit-Limit,X-RateLimit-Remaining,X-RateLimit-Reset,X-RateLimit-Credits");
    responseHeaders.set("X-RateLimit-Limit", "1");
    responseHeaders.set("X-RateLimit-Remaining", "1");
    responseHeaders.set("X-RateLimit-Reset", "1");
    responseHeaders.set("X-RateLimit-Credits", "1");

    return ResponseEntity.ok()
        .headers(responseHeaders)
        .body(routingResult);
  }

  @GetMapping(value = "/map/geocode/search", produces = "application/json")
  public ResponseEntity<Object> geocode(@RequestParam(value="q") String query){
    Object geocodeResult = geocodingService.geocode(query);
    return ResponseEntity.ok()
        .body(geocodeResult);
  }
  @GetMapping(value = "/map/geocode/reverse", produces = "application/json")
  public  ResponseEntity<Object> geocodeReverse(@RequestParam(value="lat") String lat,
      @RequestParam(value="lon") String lon,
      @RequestParam(value="zoom") int zoom){
    Object geocodeResult = geocodingReverseService.geocodeReverse(lat, lon, zoom);
    return ResponseEntity.ok()
        .body(geocodeResult);
  }
}
