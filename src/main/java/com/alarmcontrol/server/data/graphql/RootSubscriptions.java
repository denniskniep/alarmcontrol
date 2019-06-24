package com.alarmcontrol.server.data.graphql;

import com.alarmcontrol.server.data.graphql.alert.publisher.AlertAdded;
import com.alarmcontrol.server.data.graphql.alert.publisher.AlertAddedPublisher;
import com.alarmcontrol.server.data.graphql.alert.publisher.AlertChanged;
import com.alarmcontrol.server.data.graphql.alert.publisher.AlertChangedPublisher;
import com.alarmcontrol.server.data.graphql.employeeFeedback.publisher.EmployeeFeedbackAdded;
import com.alarmcontrol.server.data.graphql.employeeFeedback.publisher.EmployeeFeedbackAddedPublisher;
import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;

@Component
public class RootSubscriptions implements GraphQLSubscriptionResolver {

  private AlertAddedPublisher alertAddedPublisher;
  private AlertChangedPublisher alertChangedPublisher;
  private EmployeeFeedbackAddedPublisher employeeFeedbackAddedPublisher;

  public RootSubscriptions(AlertAddedPublisher alertAddedPublisher,
      AlertChangedPublisher alertChangedPublisher,
      EmployeeFeedbackAddedPublisher employeeFeedbackForAlertAddedPublisher) {
    this.alertAddedPublisher = alertAddedPublisher;
    this.alertChangedPublisher = alertChangedPublisher;
    this.employeeFeedbackAddedPublisher = employeeFeedbackForAlertAddedPublisher;
  }

  public Publisher<AlertAdded> alertAdded() {
    return alertAddedPublisher.getPublisher();
  }

  public Publisher<AlertChanged> alertChanged() {
    return alertChangedPublisher.getPublisher();
  }

  public Publisher<EmployeeFeedbackAdded> employeeFeedbackAdded() {
    return employeeFeedbackAddedPublisher.getPublisher();
  }
}
