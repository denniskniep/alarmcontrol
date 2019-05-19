package com.alarmcontrol.server.maps;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "map.geocoding")
public class GeocodingProperties {

  private String urlSearch;
  private String urlReverse;

  public String getUrlSearch() {
    return urlSearch;
  }

  public void setUrlSearch(String urlsearch) {
    this.urlSearch = urlsearch;
  }

  public String getUrlReverse() {
    return urlReverse;
  }

  public void setUrlReverse(String urlreverse) {
    this.urlReverse = urlreverse;
  }
}

