package com.alarmcontrol.server.maps.graphhopper.routing;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "map.graphhopper.routing")
public class GraphhopperRoutingProperties {

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

