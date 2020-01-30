package com.alarmcontrol.server.notifications.core;

public interface Event {
  long getOrganisationId();
  Class<? extends NotificationBuilder> getNotificationBuilderClass();
}
