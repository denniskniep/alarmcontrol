package com.alarmcontrol.server.aao.ruleengine;

public class Feiertag {
  private String date;
  private String description;

  public Feiertag() {
  }

  public Feiertag(String date) {
    this.date = date;
  }

  public Feiertag(String date, String description) {
    this.date = date;
    this.description = description;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
