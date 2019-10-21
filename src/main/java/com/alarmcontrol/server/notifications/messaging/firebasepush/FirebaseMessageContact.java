package com.alarmcontrol.server.notifications.messaging.firebasepush;

import com.alarmcontrol.server.notifications.core.config.Contact;

public class FirebaseMessageContact implements Contact {

  private String uniqueId;
  private String name;
  private String token;

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

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  @Override
  public String toString() {
    return "FirebaseMessageContact{" +
        "uniqueId='" + uniqueId + '\'' +
        ", name='" + name + '\'' +
        ", token='" + token + '\'' +
        '}';
  }
}

