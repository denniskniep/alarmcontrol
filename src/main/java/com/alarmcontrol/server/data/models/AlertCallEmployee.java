package com.alarmcontrol.server.data.models;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class AlertCallEmployee {

  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  private Long employeeId;

  private Long alertCallId;

  @Column(columnDefinition="clob")
  private String raw;

  private int feedback;

  @Temporal(TemporalType.TIMESTAMP)
  Date dateTime;

  protected AlertCallEmployee() {}

  public AlertCallEmployee(Long employeeId, Long alertCallId, Feedback feedback, String raw, Date dateTime) {
    this.employeeId = employeeId;
    this.alertCallId = alertCallId;
    this.raw = raw;
    this.dateTime = dateTime;
    this.setFeedback(feedback);
  }

  public Long getId() {
    return id;
  }

  public Long getEmployeeId() {
    return employeeId;
  }

  public Long getAlertCallId() {
    return alertCallId;
  }

  public Feedback getFeedback () {
    return Feedback.parse(this.feedback);
  }

  public void setFeedback(Feedback feedback) {
    this.feedback = feedback.getValue();
  }

  public String getRaw() {
    return raw;
  }

  public Date getDateTime() {
    return dateTime;
  }
}
