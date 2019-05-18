package com.alarmcontrol.server.maps;

public class RoutingResult {

  private String json;
  private String distance;
  private String duration;

  public RoutingResult(String json, String distance, String duration) {
    this.json = json;
    this.distance = distance;
    this.duration = duration;
  }

  public String getJson() {
    return json;
  }

  public String getDistance() {
    return distance;
  }

  public String getDuration() {
    return duration;
  }
}
