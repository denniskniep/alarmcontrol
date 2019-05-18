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
public class GeocodingReverseService {
  private Logger logger = LoggerFactory.getLogger(GeocodingReverseService.class);
  private CachingRestService restService;

  public GeocodingReverseService(CachingRestService restService) {
    this.restService = restService;
  }

  public GeocodingResult geocodeReverse(String lat, String lng, int zoom){
    logger.info("Start reverse geocoding '{}'", asString(lat, lng, zoom));
    Request geocodeRequest = createGeocodeRequest(lat, lng, zoom);
    Response response = restService.executeRequest(geocodeRequest);
    return new GeocodingResult(response.getJson(), lat, lng);
  }

  private Request createGeocodeRequest(String lat, String lng, int zoom) {
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://nominatim.openstreetmap.org/reverse");
    builder.queryParam("lat", lat);
    builder.queryParam("lon", lng);
    builder.queryParam("zoom", zoom);
    builder.queryParam("addressdetails",1);
    builder.queryParam("format","json");

    HttpHeaders headers = new HttpHeaders();
    headers.set("Accept", MediaType.ALL_VALUE);
    headers.set("User-Agent", "curl/7.58.0");
    HttpEntity<Object> entity = new HttpEntity<>(headers);
    URI uri = builder.build().toUri();

    return new Request(asString(lat, lng, zoom),
        uri,
        HttpMethod.GET,
        entity);
  }

  private String asString(String lat, String lng, int zoom) {
    return lat + ";"+ lng +";"+zoom;
  }
}
