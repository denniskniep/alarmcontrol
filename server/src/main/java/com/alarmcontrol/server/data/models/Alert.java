package com.alarmcontrol.server.data.models;

import java.util.Date;
import javax.persistence.*;

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
  @Column(name="date_time")
  private Date utcDateTime;

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

  @Convert(converter = JpaConverterListToJson.class)
  private StringList aao;

  protected Alert() {}

  public Alert(Long organisationId,
               String referenceId,
               boolean active,
               String keyword,
               Date utcDateTime,
               String description,
               String address,
               String addressInfo1,
               String addressInfo2,
               String addressLat,
               String addressLng,
               String addressGeocoded,
               String route,
               Double distance,
               Integer duration, StringList aao) {
    this.organisationId = organisationId;
    this.referenceId = referenceId;
    this.active = active;
    this.keyword = keyword;
    this.utcDateTime = utcDateTime;
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
    this.aao = aao;
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

  public Date getUtcDateTime() {
    return utcDateTime;
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

  public StringList getAao() {
    return aao;
  }

  public void setAddressInfo1(String addressInfo1) {
    this.addressInfo1 = addressInfo1;
  }

  public void setAddressInfo2(String addressInfo2) {
    this.addressInfo2 = addressInfo2;
  }

  public void setAddressLat(String addressLat) {
    this.addressLat = addressLat;
  }

  public void setAddressLng(String addressLng) {
    this.addressLng = addressLng;
  }

  public void setAddressGeocoded(String addressGeocoded) {
    this.addressGeocoded = addressGeocoded;
  }

  public void setRoute(String route) {
    this.route = route;
  }

  public void setDistance(Double distance) {
    this.distance = distance;
  }

  public void setDuration(Integer duration) {
    this.duration = duration;
  }

  public void setAao(StringList aao) {
    this.aao = aao;
  }
}

