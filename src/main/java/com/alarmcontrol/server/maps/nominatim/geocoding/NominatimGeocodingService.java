package com.alarmcontrol.server.maps.nominatim.geocoding;

import com.alarmcontrol.server.maps.CachingRestService;
import com.alarmcontrol.server.maps.CachingRestService.Request;
import com.alarmcontrol.server.maps.CachingRestService.Response;
import com.alarmcontrol.server.maps.GeocodingResult;
import com.alarmcontrol.server.maps.GeocodingService;
import com.jayway.jsonpath.JsonPath;
import java.net.URI;
import java.util.Arrays;
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
public class NominatimGeocodingService implements GeocodingService {
  private Logger logger = LoggerFactory.getLogger(NominatimGeocodingService.class);
  private CachingRestService restService;
  private NominatimGeocodingProperties geocodingProperties;

  public NominatimGeocodingService(CachingRestService restService,
      NominatimGeocodingProperties geocodingProperties) {
    this.restService = restService;
    this.geocodingProperties = geocodingProperties;
  }

  public GeocodingResult geocode(String query){
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

    String houseNumber = tryRead(json, "$[0].address['house_number']");
    String road = tryRead(json, "$[0].address['road']");
    String cityDistrict = tryRead(json, "$[0].address['city_district']");
    String city = tryRead(json, "$[0].address['city']");

    String addressInfo1 = join(" ", houseNumber, road);
    String addressInfo2 = join("-", city, cityDistrict);

    return new GeocodingResult(json, lat, lon, addressInfo1, addressInfo2);
  }

  private String join(String delimiter, String ...elements) {
    List<String> filteredElements = Arrays.stream(elements).filter(e -> !StringUtils.isEmpty(e)).collect(Collectors.toList());
    return String.join(delimiter, filteredElements);
  }

  private String tryRead(String json, String jsonPath) {
    try{
      return JsonPath.read(json, jsonPath);
    }catch (Exception e){
      logger.debug("Can not read jsonpath '{}' from geocoded json result", jsonPath);
      return null;
    }
  }
}
