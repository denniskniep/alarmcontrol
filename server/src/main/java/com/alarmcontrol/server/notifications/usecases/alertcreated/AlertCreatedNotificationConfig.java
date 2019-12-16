package com.alarmcontrol.server.notifications.usecases.alertcreated;

import com.alarmcontrol.server.notifications.core.config.NotificationConfig;
import java.util.List;

public class AlertCreatedNotificationConfig implements NotificationConfig {

  private List<Integer> updateDelaysInSeconds;

  public List<Integer> getUpdateDelaysInSeconds() {
    return updateDelaysInSeconds;
  }

  public void setUpdateDelaysInSeconds(List<Integer> updateDelaysInSeconds) {
    this.updateDelaysInSeconds = updateDelaysInSeconds;
  }
}
