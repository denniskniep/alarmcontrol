package com.alarmcontrol.server.data.graphql.alert.publisher;

public class AlertChanged {

  private Long id;

  public AlertChanged(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }
}
