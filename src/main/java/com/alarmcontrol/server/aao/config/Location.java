package com.alarmcontrol.server.aao.config;

public class Location {
    private String uniqueId;
    private String name;

  public Location() {
  }

  public Location(String uniqueId, String name) {
    this.uniqueId = uniqueId;
    this.name = name;
  }

  public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

  @Override
  public String toString() {
    return "Location{" +
        "uniqueId='" + uniqueId + '\'' +
        ", name='" + name + '\'' +
        '}';
  }
}

