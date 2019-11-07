package com.alarmcontrol.server.aao.config;

import com.alarmcontrol.server.aao.config.Aao;
import com.alarmcontrol.server.aao.graphql.CatalogKeywordInput;
import com.alarmcontrol.server.aao.config.Location;
import com.alarmcontrol.server.aao.config.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class AaoOrganisationConfiguration {

  public static final String KEY = "AAO";

  private List<Aao> aaoRules;
  private List<Vehicle> vehicles;
  private List<Location> locations;
  private List<Keyword> keywords;

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

  public List<Keyword> getKeywords() {
    return keywords;
  }

  public void setKeywords(List<Keyword> keywords) {
    this.keywords = keywords;
  }
}
