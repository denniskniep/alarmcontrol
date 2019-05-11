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

  public MapController(RoutingService routingService) {
    this.routingService = routingService;
  }

  @GetMapping(value = "/map/route", produces = "application/json")
  public ResponseEntity<Object> route(@RequestParam(value="point") List<String> points){
    Object routeingResult = routingService.route(points);

    //Necessary for the client library
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set("access-control-expose-headers", "X-RateLimit-Limit,X-RateLimit-Remaining,X-RateLimit-Reset,X-RateLimit-Credits");
    responseHeaders.set("X-RateLimit-Limit", "1");
    responseHeaders.set("X-RateLimit-Remaining", "1");
    responseHeaders.set("X-RateLimit-Reset", "1");
    responseHeaders.set("X-RateLimit-Credits", "1");

    return ResponseEntity.ok()
        .headers(responseHeaders)
        .body(routeingResult);
  }

  @GetMapping(value = "/map/geocode", produces = "application/json")
  public String geocode(@RequestParam(value="query") String query){

    return "{}";
  }
}
