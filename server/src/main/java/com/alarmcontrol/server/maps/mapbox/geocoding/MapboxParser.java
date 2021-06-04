package com.alarmcontrol.server.maps.mapbox.geocoding;

import com.alarmcontrol.server.maps.GeocodingResult;
import com.jayway.jsonpath.JsonPath;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.minidev.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapboxParser {

  private Logger logger = LoggerFactory.getLogger(MapboxParser.class);

  public GeocodingResult parse(String json){
    JSONArray features = JsonPath.read(json, "$.features");
    if(features.size() == 0){
      logger.info("Geocoding result is empty!");
      return new GeocodingResult(json, null, null, null, null);
    }

    JSONArray placetypes = tryReadJsonPath(json, "$.features[0].place_type");
    logger.info("Geocoding result contains placetypes '{}'", placetypes);

    String placetype = null;
    if(placetypes != null && placetypes.size() > 0){
      placetype = placetypes.get(0).toString();
      logger.info("Used geocoding result placetype is '{}'", placetype);
    }

    if(StringUtils.isBlank(placetype) ||
        StringUtils.equalsIgnoreCase(placetype, "place") ||
        StringUtils.equalsIgnoreCase(placetype, "locality")){
      logger.info("Geocoding result is of placetype '{}'. Do not return this, because it is too inaccurate!", placetype);
      return new GeocodingResult(json, null, null, null, null);
    }

    String lon = tryReadJsonPathAsDouble(json, "$.features[0].center[0]").toString();
    String lat = tryReadJsonPathAsDouble(json, "$.features[0].center[1]").toString();

    String houseNumber = tryReadJsonPath(json, "$.features[0].address");
    String road = tryReadJsonPath(json, "$.features[0].text");

    List<String> context =  JsonPath.read(json, "$.features[0].context[*].text");

    String accuracy =  tryReadJsonPath(json, "$.features[0].properties.accuracy");
    logger.info("Geocoding result had a accuracy of '{}'", accuracy);

    Double relevance =  tryReadJsonPathAsDouble(json, "$.features[0].relevance");
    logger.info("Geocoding result had a relevance of '{}'", relevance);

    String city =  tryReadJsonPathAsArrayAndReturnFirstElement(json, "$.features[0].context[?(@.id=~/place.*/i)].text");
    String cityDistrict = tryReadJsonPathAsArrayAndReturnFirstElement(json, "$.features[0].context[?(@.id=~/local.*/i)].text");

    if(StringUtils.equalsIgnoreCase(city, cityDistrict)){
      cityDistrict = null;
    }

    String addressInfo1 = join(" ", road, houseNumber);
    String addressInfo2 = join("-", city, cityDistrict);

    return new GeocodingResult(json, lat, lon, addressInfo1, addressInfo2);
  }

  private String join(String delimiter, String ...elements) {
    List<String> filteredElements = Arrays.stream(elements).filter(e -> !StringUtils.isEmpty(e)).collect(Collectors.toList());
    return String.join(delimiter, filteredElements);
  }


  private Double tryReadJsonPathAsDouble(String json, String path){
      Object value = tryReadJsonPath(json, path);

      // When an value is with an floating point then it is a double
      // But if the value has NO floating point it is an integer and there
      // will be a ClassCastException if we simply do <Double>tryReadJsonPath(json, path);
      if (value instanceof Double) {
        return (Double) value;
      } else if (value instanceof Integer) {
        return Double.valueOf((Integer) value);
      } else {
        return null;
      }
  }

  private<T> T tryReadJsonPath(String json, String path){
    try{
      logger.debug("Read '{}'", path);
      T value = JsonPath.read(json, path);
      logger.debug("Value of '{}' is '{}'", path, value);
      return value;
    }catch (Exception e){
      logger.info("Can't read jsonPath '{}'  from geocoded json result.", path);
      return null;
    }
  }

  private <T> List<T> tryReadJsonPathAsArray(String json, String path){
    try{
      logger.debug("Read '{}'", path);
      List<T> value = JsonPath.read(json, path);
      logger.debug("Value of '{}' is '{}'", path, value);
      return value;
    }catch (Exception e){
      logger.info("Can't read jsonPath '{}' from geocoded json result.", path);
      return null;
    }
  }

  private <T> T tryReadJsonPathAsArrayAndReturnFirstElement(String json, String path){
    List<T> values = tryReadJsonPathAsArray(json, path);
    if(values.isEmpty()){
      return null;
    }
    return values.get(0);
  }
}
