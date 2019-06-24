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

  private String referenceId;

  private boolean active;

  private String keyword;

  @Temporal(TemporalType.TIMESTAMP)
  Date dateTime;

  private String description;

  private String address;

  private String addressInfo1;

  private String addressInfo2;

  private String addressLat;

  private String addressLng;

  @Column(columnDefinition="clob")
  private String addressGeocoded;

  @Column(columnDefinition="clob")
  private String route;

  private Double distance;

  private Integer duration;

  protected Alert() {}

  public Alert(Long organisationId,
      String referenceId,
      boolean active,
      String keyword,
      Date dateTime,
      String description,
      String address,
      String addressInfo1,
      String addressInfo2,
      String addressLat,
      String addressLng,
      String addressGeocoded,
      String route,
      Double distance,
      Integer duration) {
    this.organisationId = organisationId;
    this.referenceId = referenceId;
    this.active = active;
    this.keyword = keyword;
    this.dateTime = dateTime;
    this.description = description;
    this.address = address;
    this.addressInfo1 = addressInfo1;
    this.addressInfo2 = addressInfo2;
    this.addressLat = addressLat;
    this.addressLng = addressLng;
    this.addressGeocoded = addressGeocoded;
    this.route = route;
    this.distance = distance;
    this.duration = duration;
  }

  public Long getId() {
    return id;
  }

  public Long getOrganisationId() {
    return organisationId;
  }

  public String getReferenceId() {
    return referenceId;
  }

  public boolean isActive() {
    return active;
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

  public String getAddress() {
    return address;
  }

  public String getAddressInfo1() {
    return addressInfo1;
  }

  public String getAddressInfo2() {
    return addressInfo2;
  }

  public String getAddressLat() {
    return addressLat;
  }

  public String getAddressLng() {
    return addressLng;
  }

  public String getAddressGeocoded() {
    return addressGeocoded;
  }

  public String getRoute() {
    return route;
  }

  public Double getDistance() {
    return distance;
  }

  public Integer getDuration() {
    return duration;
  }
}
