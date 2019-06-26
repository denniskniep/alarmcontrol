package com.alarmcontrol.server.data.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AlertNumber {

  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  private Long organisationId;

  private String number;

  private String description;

  private String shortDescription;

  protected AlertNumber() {
  }

  public AlertNumber(Long organisationId, String number, String description, String shortDescription) {
    this.organisationId = organisationId;
    this.number = number;
    this.description = description;
    this.shortDescription = shortDescription;
  }

  public Long getId() {
    return id;
  }

  public Long getOrganisationId() {
    return organisationId;
  }

  public String getNumber() {
    return number;
  }

  public String getDescription() {
    return description;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }
}
