package com.alarmcontrol.server.api;

import com.alarmcontrol.server.data.models.Status;
import java.util.Date;

public class ExternalEmployeeStatusRequest {
  private String raw;
  private String employeeReferenceId;
  private Status status;
  private Date dateTime;

  public ExternalEmployeeStatusRequest(String raw, String employeeReferenceId,
      Status status, Date dateTime) {
    this.raw = raw;
    this.employeeReferenceId = employeeReferenceId;
    this.status = status;
    this.dateTime = dateTime;
  }

  public String getRaw() {
    return raw;
  }

  public String getEmployeeReferenceId() {
    return employeeReferenceId;
  }

  public Status getStatus() {
    return status;
  }

  public Date getDateTime() {
    return dateTime;
  }
}
