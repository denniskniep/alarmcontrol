package com.alarmcontrol.server.notifications.core;

import com.alarmcontrol.server.notifications.core.config.NotificationConfig;
import com.alarmcontrol.server.notifications.core.config.Contact;
import java.util.List;

public interface NotificationBuilder {
  void sendNotifications(Event event, NotificationConfig config, List<Contact> contacts);
  Class<? extends NotificationConfig> getNotificationConfigClass();
}
