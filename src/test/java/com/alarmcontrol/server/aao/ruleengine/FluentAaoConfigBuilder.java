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

    public FluentAaoConfigBuilder withLocation(String uniqueId, String name) {
        var location = new Location();
        location.setUniqueId(uniqueId);
        location.setName(name);
        _locations.add(location);
        return this;
    }

    public FluentAaoConfigBuilder withKeyword(String uniqueId, String keywordName) {
        var keyword = new Keyword();
        keyword.setUniqueId(uniqueId);
        keyword.setKeyword(keywordName);
        _keywords.add(keyword);
        return this;
    }

    public FluentAaoConfigBuilder withVehicle(String uniqueId, String vehicleName) {
        var vehicle = new Vehicle();
        vehicle.setUniqueId(uniqueId);
        vehicle.setName(vehicleName);
        _vehicles.add(vehicle);
        return this;
    }

    public AaoOrganisationConfiguration build(){
        AaoOrganisationConfiguration config = new AaoOrganisationConfiguration();
        config.setVehicles(_vehicles);
        config.setLocations(_locations);
        config.setKeywords(_keywords);
        return config;
    }
}
