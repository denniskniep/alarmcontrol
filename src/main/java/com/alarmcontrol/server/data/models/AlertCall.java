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
public class AlertCall {

  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  private Long alertId;

  private Long organisationId;

  private Long alertNumberId;

  private String referenceId;

  @Column(columnDefinition="clob")
  private String raw;

  @Temporal(TemporalType.TIMESTAMP)
  Date dateTime;

  protected AlertCall() {
  }

  public AlertCall(Long alertId, Long organisationId, Long alertNumberId, String referenceId, String raw,
      Date dateTime) {
    this.alertId = alertId;
    this.organisationId = organisationId;
    this.alertNumberId = alertNumberId;
    this.referenceId = referenceId;
    this.raw = raw;
    this.dateTime = dateTime;
  }

  public Long getId() {
    return id;
  }

  public Long getAlertId() {
    return alertId;
  }

  public Long getAlertNumberId() {
    return alertNumberId;
  }

  public String getReferenceId() {
    return referenceId;
  }

  public String getRaw() {
    return raw;
  }

  public Date getDateTime() {
    return dateTime;
  }
}
