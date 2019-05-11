package com.alarmcontrol.server.maps;

import com.alarmcontrol.server.maps.CachingRestService.Request;
import com.alarmcontrol.server.maps.CachingRestService.Response;
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

  public GeocodingService(CachingRestService restService) {
    this.restService = restService;
  }

  public Object geocode(String query){
    logger.info("Start geocoding '{}'", query);
    Request geocodeRequest = createGeocodeRequest(query);
    Response response = restService.executeRequest(geocodeRequest);
    return response.getResponse().getBody();
  }

  private CachingRestService.Request createGeocodeRequest(String query) {
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://nominatim.openstreetmap.org/search");
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
}
