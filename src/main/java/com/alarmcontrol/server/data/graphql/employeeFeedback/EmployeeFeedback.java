package com.alarmcontrol.server.data.graphql.employeeFeedback;

import com.alarmcontrol.server.data.models.Feedback;
import java.util.Date;

public class EmployeeFeedback {
  private Long employeeId;
  private Feedback feedback;
  Date dateTime;

  public EmployeeFeedback(Long employeeId, Feedback feedback, Date dateTime) {
    this.employeeId = employeeId;
    this.feedback = feedback;
    this.dateTime = dateTime;
  }

  public Long getEmployeeId() {
    return employeeId;
  }

  public Feedback getFeedback() {
    return feedback;
  }

  public Date getDateTime() {
    return dateTime;
  }
}
