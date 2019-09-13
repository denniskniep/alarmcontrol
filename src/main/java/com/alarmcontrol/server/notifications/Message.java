package com.alarmcontrol.server.notifications;

import org.springframework.util.Assert;

public class Message {

  private long organisationId;
  private String type;
  private String subject;
  private String body;

  public Message(long organisationId, String type, String subject, String body) {
    this.organisationId = organisationId;
    this.type = type;
    Assert.notNull(subject, "subject is null");
    Assert.notNull(body, "body is null");
    this.subject = subject;
    this.body = body;
  }

  public long getOrganisationId() {
    return organisationId;
  }

  public String getType() {
    return type;
  }

  public String getSubject() {
    return subject;
  }

  public String getBody() {
    return body;
  }
}
