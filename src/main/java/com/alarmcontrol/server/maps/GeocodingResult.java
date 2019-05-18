package com.alarmcontrol.server.maps;

public class GeocodingResult {
  private String json;
  private Coordinate coordinate;

  public GeocodingResult(String json, Coordinate coordinate) {
    this.json = json;
    this.coordinate = coordinate;
  }

  public GeocodingResult(String json, String lat, String lng) {
    this.json = json;
    this.coordinate = new Coordinate(lat, lng);
  }

  public String getJson() {
    return json;
  }

  public Coordinate getCoordinate() {
    return coordinate;
  }
}

