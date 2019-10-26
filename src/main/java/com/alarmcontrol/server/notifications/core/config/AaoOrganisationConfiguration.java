package com.alarmcontrol.server.notifications.core.config;

import com.alarmcontrol.server.aaos.Aao;
import com.alarmcontrol.server.aaos.CatalogKeywordInput;
import com.alarmcontrol.server.aaos.Location;
import com.alarmcontrol.server.aaos.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class AaoOrganisationConfiguration {

  public static final String KEY = "AAO";

  private List<Aao> aaoRules;
  private List<Vehicle> vehicles;
  private List<Location> locations;
  private List<CatalogKeywordInput> keywords;

  public AaoOrganisationConfiguration() {
    aaoRules = new ArrayList<>();
    vehicles = new ArrayList<>();
    locations = new ArrayList<>();
    keywords = new ArrayList<>();
  }

  public List<Aao> getAaoRules() {
    return aaoRules;
  }

  public List<Vehicle> getVehicles() {
    return vehicles;
  }

  public void setVehicles(List<Vehicle> vehicles) {
    this.vehicles = vehicles;
  }

  public void setAaoRules(List<Aao> aaoRules) {
    this.aaoRules = aaoRules;
  }

  public List<Location> getLocations() {
    return locations;
  }

  public void setLocations(List<Location> locations) {
    this.locations = locations;
  }

  public List<CatalogKeywordInput> getKeywords() {
    return keywords;
  }

  public void setKeywords(List<CatalogKeywordInput> keywords) {
    this.keywords = keywords;
  }
}
