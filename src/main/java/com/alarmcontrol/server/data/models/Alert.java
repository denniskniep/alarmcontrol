package com.alarmcontrol.server.data.models;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Alert {

  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  private Long id;

  private Long organisationId;

  private String raw;

  private boolean active;

  private String keyword;

  @Temporal(TemporalType.TIMESTAMP)
  Date dateTime;

  private String description;

  private String addressType;

  private String address;

  protected Alert() {}

  public Alert(Long organisationId, boolean active, String raw, String keyword, Date dateTime, String description, String addressType, String address) {
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
}
