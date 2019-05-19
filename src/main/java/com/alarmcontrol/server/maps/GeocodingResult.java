package com.alarmcontrol.server.maps;

public class GeocodingResult {
  private String json;
  private Coordinate coordinate;
  private String addressInfo1;
  private String addressInfo2;

  public GeocodingResult(String json, String lat, String lng, String addressInfo1, String addressInfo2) {
    this.json = json;
    this.addressInfo1 = addressInfo1;
    this.addressInfo2 = addressInfo2;
    this.coordinate = new Coordinate(lat, lng);
  }

  public String getJson() {
    return json;
  }

  public Coordinate getCoordinate() {
    return coordinate;
  }

  public String getAddressInfo1() {
    return addressInfo1;
  }

  public String getAddressInfo2() {
    return addressInfo2;
  }
}

