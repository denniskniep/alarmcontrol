package com.alarmcontrol.server.maps.mapbox.geocoding;

import com.alarmcontrol.server.maps.CachingRestService;
import com.alarmcontrol.server.maps.CachingRestService.Request;
import com.alarmcontrol.server.maps.CachingRestService.Response;
import com.alarmcontrol.server.maps.GeocodingResult;
import com.alarmcontrol.server.maps.GeocodingService;
import com.jayway.jsonpath.JsonPath;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class MapboxGeocodingService implements GeocodingService {

  private Logger logger = LoggerFactory.getLogger(MapboxGeocodingService.class);
  private CachingRestService restService;
  private MapboxGeocodingProperties geocodingProperties;

  public MapboxGeocodingService( CachingRestService restService,
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
    String lon = JsonPath.<Double>read(json, "$.features[0].center[0]").toString();
    String lat = JsonPath.<Double>read(json, "$.features[0].center[1]").toString();

    String houseNumber = JsonPath.read(json, "$.features[0].address");
    String road = JsonPath.read(json, "$.features[0].text");

    List<String> context =  JsonPath.read(json, "$.features[0].context[*].text");

    String cityDistrict = "";
    String city = "";

    //directly in the main town
    if(context.size() == 4){
      city = context.get(1);
    }else if(context.size() > 4){
      city = context.get(2);
      cityDistrict = context.get(0);
    }

    String addressInfo1 = join(" ", road, houseNumber);
    String addressInfo2 = join("-", city, cityDistrict);

    return new GeocodingResult(json, lat, lon, addressInfo1, addressInfo2);
  }

  private String join(String delimiter, String ...elements) {
    List<String> filteredElements = Arrays.stream(elements).filter(e -> !StringUtils.isEmpty(e)).collect(Collectors.toList());
    return String.join(delimiter, filteredElements);
  }
}
