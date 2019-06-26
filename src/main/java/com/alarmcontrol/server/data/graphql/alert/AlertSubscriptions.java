package com.alarmcontrol.server.data.graphql.alert;

import com.alarmcontrol.server.data.graphql.alert.publisher.AlertAdded;
import com.alarmcontrol.server.data.graphql.alert.publisher.AlertAddedPublisher;
import com.alarmcontrol.server.data.graphql.alert.publisher.AlertChanged;
import com.alarmcontrol.server.data.graphql.alert.publisher.AlertChangedPublisher;
import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;

@Component
public class AlertSubscriptions implements GraphQLSubscriptionResolver {
  private AlertAddedPublisher alertAddedPublisher;
  private AlertChangedPublisher alertChangedPublisher;

  public AlertSubscriptions(AlertAddedPublisher alertAddedPublisher,
      AlertChangedPublisher alertChangedPublisher) {
    this.alertAddedPublisher = alertAddedPublisher;
    this.alertChangedPublisher = alertChangedPublisher;
  }

  public Publisher<AlertAdded> alertAdded() {
    return alertAddedPublisher.getPublisher();
  }

  public Publisher<AlertChanged> alertChanged() {
    return alertChangedPublisher.getPublisher();
  }
}


