package com.alarmcontrol.server.data.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Organisation {

  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String addressLat;

  private String addressLng;

  protected Organisation() {}

  public Organisation(String name, String addressLat, String addressLng) {
    this.name = name;
    this.addressLat = addressLat;
    this.addressLng = addressLng;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getAddressLat() {
    return addressLat;
  }

  public String getAddressLng() {
    return addressLng;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setAddressLat(String addressLat) {
    this.addressLat = addressLat;
  }

  public void setAddressLng(String addressLng) {
    this.addressLng = addressLng;
  }
}
