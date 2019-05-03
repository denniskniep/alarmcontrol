package com.alarmcontrol.server.data.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Organisation {

  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  private Long id;
  private String name;

  protected Organisation() {}

  public Organisation(String name) {
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
