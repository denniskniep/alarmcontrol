package com.alarmcontrol.server.notifications.core.config;

import java.util.ArrayList;
import java.util.List;

public class NotificationSubscription {
  private String uniqueId;
  private NotificationConfig notificationConfig;
  private List<String> subscriberContactUniqueIds;

  public NotificationSubscription() {
    this.subscriberContactUniqueIds = new ArrayList<>();
  }

  public String getUniqueId() {
    return uniqueId;
  }

  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  public NotificationConfig getNotificationConfig() {
    return notificationConfig;
  }

  public void setNotificationConfig(NotificationConfig notificationConfig) {
    this.notificationConfig = notificationConfig;
  }

  public List<String> getSubscriberContactUniqueIds() {
    return subscriberContactUniqueIds;
  }

  public void setSubscriberContactUniqueIds(List<String> subscriberContactUniqueIds) {
    this.subscriberContactUniqueIds = subscriberContactUniqueIds;
  }
}
