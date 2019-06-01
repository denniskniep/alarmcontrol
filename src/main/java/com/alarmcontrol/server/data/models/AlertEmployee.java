package com.alarmcontrol.server.data.models;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class AlertEmployee {

  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  private Long employeeId;

  private Long alertId;

  private int feedback;

  @Temporal(TemporalType.TIMESTAMP)
  Date dateTime;

  protected AlertEmployee() {}

  public AlertEmployee(Long employeeId, Long alertId, Feedback feedback, Date dateTime) {
    this.employeeId = employeeId;
    this.alertId = alertId;
    this.dateTime = dateTime;
    this.setFeedback(feedback);
  }

  public Long getId() {
    return id;
  }

  public Long getEmployeeId() {
    return employeeId;
  }

  public Long getAlertId() {
    return alertId;
  }

  public Feedback getFeedback () {
    return Feedback.parse(this.feedback);
  }

  public void setFeedback(Feedback feedback) {
    this.feedback = feedback.getValue();
  }

  public enum Feedback {
    NO_RESPONSE(0),
    COMMIT(1),
    LATER(2),
    CANCEL (3);

    private int value;

    Feedback(int value) { this.value = value; }

    public int getValue() { return value; }

    public static Feedback parse(int id) {
      Feedback feedback = null;
      for (Feedback item : Feedback.values()) {
        if (item.getValue()==id) {
          feedback = item;
          break;
        }
      }
      return feedback;
    }
  }
}
