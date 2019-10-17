package com.alarmcontrol.server.notifications.core.messaging;

import org.springframework.util.Assert;

public class Message {
  private String subject;
  private String body;

  public Message(String subject, String body) {
    Assert.notNull(subject, "subject is null");
    Assert.notNull(body, "body is null");
    this.subject = subject;
    this.body = body;
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
        ", subject='" + subject + '\'' +
        ", body='" + body + '\'' +
        '}';
  }
}
