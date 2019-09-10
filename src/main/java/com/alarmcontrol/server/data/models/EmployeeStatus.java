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
public class EmployeeStatus {

  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  private Long employeeId;

  private int status;

  @Temporal(TemporalType.TIMESTAMP)
  Date dateTime;

  @Column(columnDefinition="clob")
  private String raw;

  protected EmployeeStatus() {
  }

  public EmployeeStatus(Long employeeId, Status status, Date dateTime, String raw) {
    this.employeeId = employeeId;
    this.dateTime = dateTime;
    this.raw = raw;
    this.setStatus(status);
  }

  public Long getId() {
    return id;
  }

  public Long getEmployeeId() {
    return employeeId;
  }

  public Date getDateTime() {
    return dateTime;
  }

  public String getRaw() {
    return raw;
  }

  public Status getStatus () {
    return Status.parse(this.status);
  }

  public void setStatus(Status status) {
    this.status = status.getValue();
  }

  public void setEmployeeId(Long employeeId) {
    this.employeeId = employeeId;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public void setDateTime(Date dateTime) {
    this.dateTime = dateTime;
  }

  public void setRaw(String raw) {
    this.raw = raw;
  }
}
