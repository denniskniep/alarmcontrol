package com.alarmcontrol.server.data.models;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Alert {

  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  private Long organisationId;

  @Column(columnDefinition="clob")
  private String raw;

  private boolean active;

  private String keyword;

  @Temporal(TemporalType.TIMESTAMP)
  Date dateTime;

  private String description;

  private String addressType;

  private String address;

  @Column(columnDefinition="clob")
  private String route;

  protected Alert() {}

  public Alert(Long organisationId,
      String raw,
      boolean active,
      String keyword,
      Date dateTime,
      String description,
      String addressType,
      String address,
      String route) {
    this.organisationId = organisationId;
    this.raw = raw;
    this.active = active;
    this.keyword = keyword;
    this.dateTime = dateTime;
    this.description = description;
    this.addressType = addressType;
    this.address = address;
    this.route = route;
  }

  public Long getId() {
    return id;
  }

  public Long getOrganisationId() {
    return organisationId;
  }

  public boolean isActive() {
    return active;
  }

  public String getRaw() {
    return raw;
  }

  public String getKeyword() {
    return keyword;
  }

  public Date getDateTime() {
    return dateTime;
  }

  public String getDescription() {
    return description;
  }

  public String getAddressType() {
    return addressType;
  }

  public String getAddress() {
    return address;
  }

  public String getRoute() {
    return route;
  }
}
