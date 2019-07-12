package com.alarmcontrol.server.data.graphql.employeeStatus.publisher;

public class EmployeeStatusAdded {

  private Long employeeId;

  public EmployeeStatusAdded(Long employeeId) {
    this.employeeId = employeeId;
  }

  public Long getEmployeeId() {
    return employeeId;
  }
}
