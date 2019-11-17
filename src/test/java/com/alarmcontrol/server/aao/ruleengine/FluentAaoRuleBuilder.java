package com.alarmcontrol.server.aao.ruleengine;

import com.alarmcontrol.server.aao.config.AaoRule;

import com.alarmcontrol.server.aao.config.Keyword;
import com.alarmcontrol.server.aao.config.Location;
import com.alarmcontrol.server.aao.config.Vehicle;
import java.util.ArrayList;
import java.util.UUID;

public class FluentAaoRuleBuilder {

    private ArrayList<String> _keywords = new ArrayList<>();
    private ArrayList<String> _locations = new ArrayList<>();
    private ArrayList<String> _vehicles = new ArrayList<>();
    public FluentAaoRuleBuilder createRule(){
        return new FluentAaoRuleBuilder();
    }

    public FluentAaoRuleBuilder withKeyword(Keyword keyword) {
        _keywords.add(keyword.getUniqueId());
        return this;
    }

    public FluentAaoRuleBuilder withLocation(Location location) {
        _locations.add(location.getUniqueId());
        return this;
    }

    public FluentAaoRuleBuilder withVehicle(Vehicle vehicle) {
        _vehicles.add(vehicle.getUniqueId());
        return this;
    }

    public AaoRule build() {
        AaoRule aao = new AaoRule();
        aao.setLocations(_locations);
        aao.setKeywords(_keywords);
        aao.setVehicles(_vehicles);
        aao.setUniqueId(UUID.randomUUID().toString());
        return aao;
    }
}

