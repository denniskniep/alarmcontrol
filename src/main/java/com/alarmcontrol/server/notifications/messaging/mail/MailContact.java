package com.alarmcontrol.server.notifications.messaging.mail;

import com.alarmcontrol.server.notifications.core.config.Contact;

public class MailContact implements Contact {

  private String uniqueId;
  private String mailAddress;
  private String name;

  @Override
  public String getUniqueId() {
    return uniqueId;
  }

  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  @Override
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getMailAddress() {
    return mailAddress;
  }

  public void setMailAddress(String mailAddress) {
    this.mailAddress = mailAddress;
  }

  @Override
  public String toString() {
    return "MailContact{" +
        "mailAddress='" + mailAddress + '\'' +
        '}';
  }
}
