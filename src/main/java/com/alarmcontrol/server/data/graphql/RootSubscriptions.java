package com.alarmcontrol.server.data.graphql;

import com.alarmcontrol.server.data.graphql.alert.AlertAddedPublisher;
import com.alarmcontrol.server.data.graphql.models.AlertAdded;
import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;

@Component
public class RootSubscriptions implements GraphQLSubscriptionResolver {

  private AlertAddedPublisher alertAddedPublisher;

  public RootSubscriptions(AlertAddedPublisher alertAddedPublisher) {
    this.alertAddedPublisher = alertAddedPublisher;
  }

  public Publisher<AlertAdded> alertAdded(){
    return alertAddedPublisher.getPublisher();
  }
}
