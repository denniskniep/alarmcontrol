package com.alarmcontrol.server.data.graphql.alert.publisher;

import com.alarmcontrol.server.data.graphql.CommonPublisher;
import org.springframework.stereotype.Component;

@Component
public class AlertAddedPublisher extends CommonPublisher<AlertAdded> {

  public void emitAlertAdded(Long alertId, Long organisationId) {
    this.emit(new AlertAdded(alertId, organisationId));
    logger.info("Notified Subscribers that Alert " + alertId + " was added!");
  }
}
