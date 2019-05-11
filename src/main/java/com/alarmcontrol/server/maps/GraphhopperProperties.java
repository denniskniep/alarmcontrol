package com.alarmcontrol.server.maps;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "map.routing.graphhopper")
public class GraphhopperProperties {

  private String url;
  private String apikey;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getApikey() {
    return apikey;
  }

  public void setApikey(String apikey) {
    this.apikey = apikey;
  }
}

