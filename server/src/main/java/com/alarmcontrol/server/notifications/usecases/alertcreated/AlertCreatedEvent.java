package com.alarmcontrol.server.notifications.usecases.alertcreated;

import com.alarmcontrol.server.data.models.Alert;
import com.alarmcontrol.server.notifications.core.Event;
import com.alarmcontrol.server.notifications.core.NotificationBuilder;
import org.springframework.util.Assert;

public class AlertCreatedEvent implements Event {

  private Alert alert;

  public AlertCreatedEvent(Alert alert) {
    Assert.notNull(alert, "alert is null");
    this.alert = alert;
  }

  public Alert getAlert() {
    return alert;
  }

  @Override
  public long getOrganisationId() {
    return alert.getOrganisationId();
  }

  public Class<? extends NotificationBuilder> getNotificationBuilderClass(){
    return AlertCreatedNotificationBuilder.class;
  }
}
