package com.alarmcontrol.server.maps.mapbox.geocoding;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "map.mapbox.geocoding")
public class MapboxGeocodingProperties {

  private String urlSearch;
  private String accessToken;

  public String getUrlSearch() {
    return urlSearch;
  }

  public void setUrlSearch(String urlsearch) {
    this.urlSearch = urlsearch;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }
}

