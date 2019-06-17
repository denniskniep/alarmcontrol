package com.alarmcontrol.server.data.graphql.models;

public class EmployeeFeedbackForAlertAdded {

  private Long alertId;
  private Long employeeId;

  public EmployeeFeedbackForAlertAdded(Long alertId, Long employeeId) {
    this.alertId = alertId;
    this.employeeId = employeeId;
  }

  public Long getAlertId() {
    return alertId;
  }

  public Long getEmployeeId() {
    return employeeId;
  }
}
