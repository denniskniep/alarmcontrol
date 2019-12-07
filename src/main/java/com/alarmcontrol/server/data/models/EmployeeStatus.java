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
  @Column(name="date_time")
  private Date utcDateTime;

  @Column(columnDefinition="clob")
  private String raw;

  protected EmployeeStatus() {
  }

  public EmployeeStatus(Long employeeId, Status status, Date utcDateTime, String raw) {
    this.employeeId = employeeId;
    this.utcDateTime = utcDateTime;
    this.raw = raw;
    this.setStatus(status);
  }

  public Long getId() {
    return id;
  }

  public Long getEmployeeId() {
    return employeeId;
  }

  public Date getUtcDateTime() {
    return utcDateTime;
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

  public void setUtcDateTime(Date utcDateTime) {
    this.utcDateTime = utcDateTime;
  }

  public void setRaw(String raw) {
    this.raw = raw;
  }
}
