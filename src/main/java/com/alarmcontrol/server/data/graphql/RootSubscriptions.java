package com.alarmcontrol.server.data.graphql;

import com.alarmcontrol.server.data.graphql.alert.publisher.AlertAddedPublisher;
import com.alarmcontrol.server.data.graphql.employeeFeedback.publisher.EmployeeFeedbackForAlertAddedPublisher;
import com.alarmcontrol.server.data.graphql.alert.publisher.AlertAdded;
import com.alarmcontrol.server.data.graphql.employeeFeedback.publisher.EmployeeFeedbackForAlertAdded;
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
