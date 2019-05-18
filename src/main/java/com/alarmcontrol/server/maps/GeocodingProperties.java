package com.alarmcontrol.server.maps;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "map.geocoding")
public class GeocodingProperties {

  private String url;;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}

