package com.alarmcontrol.server.data.graphql.employeeFeedback.publisher;

public class EmployeeFeedbackAdded {

  private Long alertId;
  private Long alertCallId;
  private Long employeeId;

  public EmployeeFeedbackAdded(Long alertId,Long alertCallId, Long employeeId) {
    this.alertId = alertId;
    this.alertCallId = alertCallId;
    this.employeeId = employeeId;
  }

  public Long getAlertId() {
    return alertId;
  }

  public Long getAlertCallId() {
    return alertCallId;
  }

  public Long getEmployeeId() {
    return employeeId;
  }
}
