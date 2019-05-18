package com.alarmcontrol.server.maps;

import com.alarmcontrol.server.maps.CachingRestService.Request;
import com.alarmcontrol.server.maps.CachingRestService.Response;
import com.jayway.jsonpath.JsonPath;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GeocodingService {
  private Logger logger = LoggerFactory.getLogger(GeocodingService.class);
  private CachingRestService restService;
  private GeocodingProperties geocodingProperties;

  public GeocodingService(CachingRestService restService,
      GeocodingProperties geocodingProperties) {
    this.restService = restService;
    this.geocodingProperties = geocodingProperties;
  }

  public GeocodingResult geocode(String query){
    logger.info("Start geocoding '{}'", query);
    Request geocodeRequest = createGeocodeRequest(query);
    Response response = restService.executeRequest(geocodeRequest);
    return parse(response.getJson());
  }

  private CachingRestService.Request createGeocodeRequest(String query) {
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(geocodingProperties.getUrl());
    builder.queryParam("q", query);
    builder.queryParam("limit",1);
    builder.queryParam("format","json");
    builder.queryParam("addressdetails",1);

    HttpHeaders headers = new HttpHeaders();
    headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
    HttpEntity<Object> entity = new HttpEntity<>(headers);
    URI uri = builder.build().toUri();

    return new CachingRestService.Request(query,
        uri,
        HttpMethod.GET,
        entity);
  }

  private GeocodingResult parse(String json){
    String lat = JsonPath.read(json, "$[0]['lat']");
    String lon = JsonPath.read(json, "$[0]['lon']");
    return new GeocodingResult(json, lat, lon);
  }
}
