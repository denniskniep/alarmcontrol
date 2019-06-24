package com.alarmcontrol.server.data.graphql;

import com.alarmcontrol.server.data.graphql.alert.AlertAddedPublisher;
import com.alarmcontrol.server.data.graphql.alert.EmployeeFeedbackForAlertAddedPublisher;
import com.alarmcontrol.server.data.graphql.models.AlertAdded;
import com.alarmcontrol.server.data.graphql.models.EmployeeFeedbackForAlertAdded;
import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;

@Component
public class RootSubscriptions implements GraphQLSubscriptionResolver {

  private AlertAddedPublisher alertAddedPublisher;
  private EmployeeFeedbackForAlertAddedPublisher employeeFeedbackForAlertAddedPublisher;

  public RootSubscriptions(AlertAddedPublisher alertAddedPublisher,
      EmployeeFeedbackForAlertAddedPublisher employeeFeedbackForAlertAddedPublisher) {
    this.alertAddedPublisher = alertAddedPublisher;
    this.employeeFeedbackForAlertAddedPublisher = employeeFeedbackForAlertAddedPublisher;
  }

  public Publisher<AlertAdded> alertAdded(){
    return alertAddedPublisher.getPublisher();
  }

  public Publisher<EmployeeFeedbackForAlertAdded> employeeFeedbackForAlertAdded(){
    return employeeFeedbackForAlertAddedPublisher.getPublisher();
  }
}
