package com.alarmcontrol.server.notifications.messaging.firebasepush;

import com.alarmcontrol.server.notifications.core.config.Contact;

public class FirebaseMessageContact implements Contact {

  private String uniqueId;
  private String name;
  private String mail;

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

  public String getMail() {
    return mail;
  }

  public void setMail(String mail) {
    this.mail = mail;
  }

  @Override
  public String toString() {
    return "FirebaseMessageContact{" +
        "uniqueId='" + uniqueId + '\'' +
        ", name='" + name + '\'' +
        ", mail='" + mail + '\'' +
        '}';
  }
}

