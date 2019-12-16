package com.alarmcontrol.server.aao.config;

import java.util.ArrayList;
import java.util.List;

public class AaoOrganisationConfiguration {

  public static final String KEY = "AAO";

  private List<AaoRule> aaoRules;
  private List<Vehicle> vehicles;
  private List<Location> locations;
  private List<TimeRange> timeRanges;
  private List<Keyword> keywords;

  public AaoOrganisationConfiguration() {
    aaoRules = new ArrayList<>();
    vehicles = new ArrayList<>();
    locations = new ArrayList<>();
    timeRanges = new ArrayList<>();
    keywords = new ArrayList<>();
  }

  public List<AaoRule> getAaoRules() {
    return aaoRules;
  }

  public List<Vehicle> getVehicles() {
    return vehicles;
  }

  public void setVehicles(List<Vehicle> vehicles) {
    this.vehicles = vehicles;
  }

  public void setAaoRules(List<AaoRule> aaoRules) {
    this.aaoRules = aaoRules;
  }

  public List<Location> getLocations() {
    return locations;
  }

  public void setLocations(List<Location> locations) {
    this.locations = locations;
  }

  public List<Keyword> getKeywords() {
    return keywords;
  }

  public void setKeywords(List<Keyword> keywords) {
    this.keywords = keywords;
  }

  public List<TimeRange> getTimeRanges() {
    return timeRanges;
  }

  public void setTimeRanges(List<TimeRange> timeRanges) {
    this.timeRanges = timeRanges;
  }
}
