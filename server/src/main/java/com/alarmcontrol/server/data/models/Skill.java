package com.alarmcontrol.server.data.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Skill {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long organisationId;

  private String name;

  private String shortName;

  private boolean displayAtOverview;

  protected Skill() {}

  public Skill(Long organisationId, String name, String shortName, boolean displayAtOverview) {
    this.organisationId = organisationId;
    this.name = name;
    this.shortName = shortName;
    this.displayAtOverview = displayAtOverview;
  }

  public Long getId() {
    return id;
  }

  public Long getOrganisationId() {
    return organisationId;
  }

  public String getName() {
    return name;
  }

  public String getShortName() {
    return shortName;
  }

  public boolean isDisplayAtOverview() {
    return displayAtOverview;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
  }

  public void setDisplayAtOverview(boolean displayAtOverview) {
    this.displayAtOverview = displayAtOverview;
  }
}
