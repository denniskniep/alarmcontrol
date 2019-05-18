package com.alarmcontrol.server.maps;

public class Coordinate {
  private String lat;
  private String lng;

  public Coordinate(String lat, String lng) {
    this.lat = lat;
    this.lng = lng;
  }

  public String getLat() {
    return lat;
  }

  public String getLng() {
    return lng;
  }
}
