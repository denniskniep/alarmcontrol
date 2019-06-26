package com.alarmcontrol.server.data.graphql.alert.publisher;

import com.alarmcontrol.server.data.graphql.CommonPublisher;
import org.springframework.stereotype.Component;

@Component
public class AlertChangedPublisher extends CommonPublisher<AlertChanged> {

  public void emitAlertChanged(Long alertId) {
    this.emit(new AlertChanged(alertId));
    logger.info("Notified Subscribers that Alert " + alertId + " was changed!");
  }
}
