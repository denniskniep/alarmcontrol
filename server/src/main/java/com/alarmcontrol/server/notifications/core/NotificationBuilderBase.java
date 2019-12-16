package com.alarmcontrol.server.notifications.core;

import com.alarmcontrol.server.notifications.core.config.NotificationConfig;
import com.alarmcontrol.server.notifications.core.config.Contact;
import java.util.List;

public abstract class NotificationBuilderBase<E extends Event, N extends NotificationConfig>
    implements NotificationBuilder {

  private Class<N> notificationConfigClass;

  public NotificationBuilderBase(Class<N> notificationConfigClass) {
    this.notificationConfigClass = notificationConfigClass;
  }

  @Override
  public void sendNotifications(Event event, NotificationConfig config, List<Contact> contacts) {
    E specificEvent = (E) event;
    N specificConfig = (N) config;
    sendNotificationsInternal(specificEvent, specificConfig, contacts);
  }

  protected abstract void sendNotificationsInternal(E event, N config, List<Contact> contacts);

  @Override
  public Class<? extends NotificationConfig> getNotificationConfigClass() {
    return notificationConfigClass;
  }
}
