package com.alarmcontrol.server.notifications.core.messaging;

import org.springframework.util.Assert;

public class Message {
  private String subject;
  private String body;
  private Severity severity;

  public Message(Severity severity, String subject, String body) {
    Assert.notNull(severity, "severity is null");
    Assert.notNull(subject, "subject is null");
    Assert.notNull(body, "body is null");
    this.severity = severity;
    this.subject = subject;
    this.body = body;
  }

  public Severity getSeverity() {
    return severity;
  }

  public String getSubject() {
    return subject;
  }

  public String getBody() {
    return body;
  }

  @Override
  public String toString() {
    return "Message{" +
        "subject='" + subject + '\'' +
        ", body='" + body + '\'' +
        '}';
  }
}
