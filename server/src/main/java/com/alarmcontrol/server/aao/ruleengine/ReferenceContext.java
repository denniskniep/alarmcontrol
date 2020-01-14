package com.alarmcontrol.server.aao.ruleengine;

import java.util.List;

public class ReferenceContext {
  private List<Feiertag> feiertage;
  private String organisationLocation;

  public ReferenceContext(List<Feiertag> feiertage, String organisationLocation) {
    this.feiertage = feiertage;
    this.organisationLocation = organisationLocation;
  }

  public List<Feiertag> getFeiertage() {
    return feiertage;
  }

  public String getOrganisationLocation() {
    return organisationLocation;
  }
}
