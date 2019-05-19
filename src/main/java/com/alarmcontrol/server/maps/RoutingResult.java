package com.alarmcontrol.server.maps;

public class RoutingResult {

  private String json;
  private Double distance;
  private Integer duration;

  public RoutingResult(String json, Double distance, Integer duration) {
    this.json = json;
    this.distance = distance;
    this.duration = duration;
  }

  public String getJson() {
    return json;
  }

  public Double getDistance() {
    return distance;
  }

  public Integer getDuration() {
    return duration;
  }
}
