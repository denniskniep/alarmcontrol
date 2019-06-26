package com.alarmcontrol.server.data.graphql.alert.publisher;

public class AlertAdded {

  private Long id;

  public AlertAdded(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}
