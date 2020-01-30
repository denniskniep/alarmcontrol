package com.alarmcontrol.server.aao.config;

import java.util.ArrayList;

public class AaoRule {

  private String uniqueId;
  private ArrayList<String> vehicles;
  private ArrayList<String> keywords;
  private ArrayList<String> locations;
  private ArrayList<String> timeRangeNames;

  public ArrayList<String> getVehicles() {
    return vehicles;
  }

  public void setVehicles(ArrayList<String> vehicles) {
    this.vehicles = vehicles;
  }

  public ArrayList<String> getKeywords() {
    return keywords;
  }

  public void setKeywords(ArrayList<String> keywords) {
    this.keywords = keywords;
  }

  public ArrayList<String> getLocations() {
    return locations;
  }

  public void setLocations(ArrayList<String> locations) {
    this.locations = locations;
  }

  public ArrayList<String> getTimeRangeNames() {
    return timeRangeNames;
  }

  public void setTimeRangeNames(ArrayList<String> timeRangeNames) {
    this.timeRangeNames = timeRangeNames;
  }

  public String getUniqueId() {
    return this.uniqueId;
  }

  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  @Override
  public String toString() {
    return "AaoRule{" +
        "uniqueId=" + uniqueId +
        ", keywords=" + keywords +
        ", locations=" + locations +
        ", timeRangeNames=" + timeRangeNames +
        ", vehicles=" + vehicles +
        '}';
  }
}
