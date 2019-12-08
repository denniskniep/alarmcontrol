package com.alarmcontrol.server.aao.ruleengine;

import com.alarmcontrol.server.aao.config.Keyword;
import com.alarmcontrol.server.aao.config.Location;
import com.alarmcontrol.server.aao.config.Vehicle;
import com.alarmcontrol.server.aao.config.AaoOrganisationConfiguration;

import java.util.ArrayList;

public class FluentAaoConfigBuilder {

  private ArrayList<Location> _locations = new ArrayList<>();
  private ArrayList<Keyword> _keywords = new ArrayList<>();
  private ArrayList<Vehicle> _vehicles = new ArrayList<>();

  public FluentAaoConfigBuilder createConfig() {
    return new FluentAaoConfigBuilder();
  }

  public FluentAaoConfigBuilder withLocation(Location location) {
    var newLocation = new Location();
    newLocation.setUniqueId(location.getUniqueId());
    newLocation.setName(location.getName());
    _locations.add(newLocation);
    return this;
  }

  public FluentAaoConfigBuilder withKeyword(Keyword keyword) {
    var newKeyword = new Keyword();
    newKeyword.setUniqueId(keyword.getUniqueId());
    newKeyword.setKeyword(keyword.getKeyword());
    _keywords.add(newKeyword);
    return this;
  }

  public FluentAaoConfigBuilder withVehicle(Vehicle vehicle) {
    var newVehicle = new Vehicle();
    newVehicle.setUniqueId(vehicle.getUniqueId());
    newVehicle.setName(vehicle.getName());
    _vehicles.add(newVehicle);
    return this;
  }

  public AaoOrganisationConfiguration build() {
    AaoOrganisationConfiguration config = new AaoOrganisationConfiguration();
    config.setVehicles(_vehicles);
    config.setLocations(_locations);
    config.setKeywords(_keywords);
    return config;
  }
}
