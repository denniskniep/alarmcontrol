package com.alarmcontrol.server.maps.nominatim.geocoding;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "map.nominatim.geocoding")
public class NominatimGeocodingProperties {

  private String urlSearch;

  public String getUrlSearch() {
    return urlSearch;
  }

  public void setUrlSearch(String urlsearch) {
    this.urlSearch = urlsearch;
  }

}

