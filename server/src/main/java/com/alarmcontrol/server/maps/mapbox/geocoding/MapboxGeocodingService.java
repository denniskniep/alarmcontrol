package com.alarmcontrol.server.maps.mapbox.geocoding;

import com.alarmcontrol.server.maps.CachingRestService;
import com.alarmcontrol.server.maps.CachingRestService.Request;
import com.alarmcontrol.server.maps.CachingRestService.Response;
import com.alarmcontrol.server.maps.GeocodingResult;
import com.alarmcontrol.server.maps.GeocodingService;
import java.net.URI;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class MapboxGeocodingService implements GeocodingService {

  private Logger logger = LoggerFactory.getLogger(MapboxGeocodingService.class);
  private CachingRestService restService;
  private MapboxGeocodingProperties geocodingProperties;

  public MapboxGeocodingService(CachingRestService restService,
      MapboxGeocodingProperties geocodingProperties) {
    this.restService = restService;
    this.geocodingProperties = geocodingProperties;
  }

  @Override
  public GeocodingResult geocode(String query) {
    logger.info("Start geocoding '{}'", query);
    Request geocodeRequest = createGeocodeRequest(query);
    Response response = restService.executeRequest(geocodeRequest);

    try{
      return parse(response.getJson());
    }catch (Exception e){
      throw new RuntimeException("Can not parse json to GeocodingResult\n"+ response.getJson(), e);
    }
  }

  private CachingRestService.Request createGeocodeRequest(String query) {
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(geocodingProperties.getUrlSearch());
    builder.queryParam("access_token", geocodingProperties.getAccessToken());
    builder.queryParam("autocomplete",false);
    builder.queryParam("country","de");
    builder.queryParam("types","address,poi,place,locality");

    HttpHeaders headers = new HttpHeaders();
    headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
    HttpEntity<Object> entity = new HttpEntity<>(headers);
    URI uri = builder.buildAndExpand(Collections.singletonMap("searchQuery", query)).encode().toUri();

    return new CachingRestService.Request(query,
        uri,
        HttpMethod.GET,
        entity);
  }

  private GeocodingResult parse(String json){
   return new MapboxParser().parse(json);
  }
}
