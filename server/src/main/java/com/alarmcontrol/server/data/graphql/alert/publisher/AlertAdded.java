package com.alarmcontrol.server.data.graphql.alert.publisher;

public class AlertAdded {

  private Long id;
  private Long organisationId;

  public AlertAdded(Long id, Long organisationId) {
    this.id = id;
    this.organisationId = organisationId;
  }

  public Long getId() {
    return id;
  }

  public Long getOrganisationId() {
    return organisationId;
  }
}
