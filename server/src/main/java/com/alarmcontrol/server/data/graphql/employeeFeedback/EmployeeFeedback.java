package com.alarmcontrol.server.data.graphql.employeeFeedback;

import com.alarmcontrol.server.data.models.Feedback;
import java.util.Date;

public class EmployeeFeedback {
  private Long employeeId;
  private Feedback feedback;
  Date utcDateTime;

  public EmployeeFeedback(Long employeeId, Feedback feedback, Date utcDateTime) {
    this.employeeId = employeeId;
    this.feedback = feedback;
    this.utcDateTime = utcDateTime;
  }

  public Long getEmployeeId() {
    return employeeId;
  }

  public Feedback getFeedback() {
    return feedback;
  }

  public Date getUtcDateTime() {
    return utcDateTime;
  }
}
