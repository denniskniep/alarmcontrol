package com.alarmcontrol.server.maps.mapbox.geocoding;

import com.alarmcontrol.server.maps.GeocodingResult;
import com.jayway.jsonpath.JsonPath;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class MapboxParser {

  private Logger logger = LoggerFactory.getLogger(MapboxParser.class);

  public GeocodingResult parse(String json){
    String lon = JsonPath.<Double>read(json, "$.features[0].center[0]").toString();
    String lat = JsonPath.<Double>read(json, "$.features[0].center[1]").toString();

    String houseNumber = tryReadJsonPath(json, "$.features[0].address");
    String road = tryReadJsonPath(json, "$.features[0].text");

    List<String> context =  JsonPath.read(json, "$.features[0].context[*].text");

    String accuracy =  tryReadJsonPath(json, "$.features[0].properties.accuracy");
    logger.info("Geocoding result had a accuracy of '{}'", accuracy);

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
    if (StringUtils.isEmpty(addressInfo1)) {
      addressInfo1 = join(";", lon, lat);
    }

    String addressInfo2 = join("-", city, cityDistrict);

    return new GeocodingResult(json, lat, lon, addressInfo1, addressInfo2);
  }

  private String join(String delimiter, String ...elements) {
    List<String> filteredElements = Arrays.stream(elements).filter(e -> !StringUtils.isEmpty(e)).collect(Collectors.toList());
    return String.join(delimiter, filteredElements);
  }

  private<T> T tryReadJsonPath(String json, String path){
    try{
      return JsonPath.read(json, path);
    }catch (Exception e){
      logger.info("Can't read jsonPath '{}'  from geocoded json result.", path);
      return null;
    }
  }

}
