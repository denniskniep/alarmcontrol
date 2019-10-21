package com.alarmcontrol.server.notifications.core.config;

import java.util.ArrayList;
import java.util.List;

public class AaoOrganisationConfiguration {

  public static final String KEY = "AAO";

  private List<AaoBase> aaoRules;

  public AaoOrganisationConfiguration() {
    aaoRules = new ArrayList<>();
  }

  public List<AaoBase> getAaoRules() {
    return aaoRules;
  }

  public void setAaoRules(List<AaoBase> aaoRules) {
    this.aaoRules = aaoRules;
  }
}
