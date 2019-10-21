package com.alarmcontrol.server.aaos;

import com.alarmcontrol.server.notifications.core.config.AaoBase;

import java.util.ArrayList;

public class Aao implements AaoBase {

    private String uniqueId;
    private ArrayList<String> vehicles;
    private ArrayList<String> keywords;
    private ArrayList<String> locations;
    private String name;

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

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

    @Override
    public String toString() {
        return "Aao{" +
                "uniqueId='" + uniqueId + '\'' +
                ", vehicles=" + vehicles +
                ", keywords=" + keywords +
                ", locations=" + locations +
                '}';
    }

    @Override
    public String getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
