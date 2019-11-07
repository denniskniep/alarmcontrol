package com.alarmcontrol.server.aao.ruleengine;

import com.alarmcontrol.server.aao.config.Aao;

import java.util.ArrayList;
import java.util.UUID;

public class FluentAaoRuleBuilder {

    private ArrayList<String> _keywords = new ArrayList<>();
    private ArrayList<String> _locations = new ArrayList<>();
    private ArrayList<String> _vehicles = new ArrayList<>();
    public FluentAaoRuleBuilder createRule(){
        return new FluentAaoRuleBuilder();
    }

    public FluentAaoRuleBuilder withKeywordId(String uniqueId) {
        _keywords.add(uniqueId);
        return this;
    }

    public FluentAaoRuleBuilder withLocationId(String uniqueId) {
        _locations.add(uniqueId);
        return this;
    }

    public FluentAaoRuleBuilder withVehicleId(String uniqueVehicleName) {
        _vehicles.add(uniqueVehicleName);
        return this;
    }

    public Aao build() {
        Aao aao = new Aao();
        aao.setLocations(_locations);
        aao.setKeywords(_keywords);
        aao.setVehicles(_vehicles);
        aao.setUniqueId(UUID.randomUUID().toString());
        return aao;
    }
}

