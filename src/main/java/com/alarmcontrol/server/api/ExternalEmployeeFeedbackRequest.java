package com.alarmcontrol.server.api;

import com.alarmcontrol.server.data.models.Feedback;
import java.util.Date;

public class ExternalEmployeeFeedbackRequest {
  private String raw;
  private String alertCallReferenceId;
  private String employeeReferenceId;
  private Feedback feedback;
  private Date dateTime;

  public ExternalEmployeeFeedbackRequest(String raw, String alertCallReferenceId, String employeeReferenceId,
      Feedback feedback, Date dateTime) {
    this.raw = raw;
    this.alertCallReferenceId = alertCallReferenceId;
    this.employeeReferenceId = employeeReferenceId;
    this.feedback = feedback;
    this.dateTime = dateTime;
  }

  public String getRaw() {
    return raw;
  }

  public String getAlertCallReferenceId() {
    return alertCallReferenceId;
  }

  public String getEmployeeReferenceId() {
    return employeeReferenceId;
  }

  public Feedback getFeedback() {
    return feedback;
  }

  public Date getDateTime() {
    return dateTime;
  }
}
