package com.alarmcontrol.server.api;

import java.util.Date;

public class ExternalAlertRequest {

  private String raw;
  private String alertNumber;
  private String alertReferenceId;
  private String alertCallReferenceId;
  private String keyword;
  private String description;
  private String address;
  private Date dateTime;

  public ExternalAlertRequest(String raw,
      String alertNumber,
      String alertReferenceId,
      String alertCallReferenceId,
      String keyword,
      String description,
      String address,
      Date dateTime) {
    this.raw = raw;
    this.alertNumber = alertNumber;
    this.alertReferenceId = alertReferenceId;
    this.alertCallReferenceId = alertCallReferenceId;
    this.keyword = keyword;
    this.description = description;
    this.address = address;
    this.dateTime = dateTime;
  }

  public String getRaw() {
    return raw;
  }

  public String getAlertNumber() {
    return alertNumber;
  }

  public String getAlertReferenceId() {
    return alertReferenceId;
  }

  public String getAlertCallReferenceId() {
    return alertCallReferenceId;
  }

  public String getKeyword() {
    return keyword;
  }

  public String getDescription() {
    return description;
  }

  public String getAddress() {
    return address;
  }

  public Date getDateTime() {
    return dateTime;
  }
}
