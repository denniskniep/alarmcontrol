package com.alarmcontrol.server.data.graphql.employeeFeedback;

import com.alarmcontrol.server.data.graphql.employeeFeedback.publisher.EmployeeFeedbackAdded;
import com.alarmcontrol.server.data.graphql.employeeFeedback.publisher.EmployeeFeedbackAddedPublisher;
import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;

@Component
public class EmployeeFeedbackSubscriptions implements GraphQLSubscriptionResolver {
  private EmployeeFeedbackAddedPublisher employeeFeedbackAddedPublisher;

  public EmployeeFeedbackSubscriptions(
      EmployeeFeedbackAddedPublisher employeeFeedbackAddedPublisher) {
    this.employeeFeedbackAddedPublisher = employeeFeedbackAddedPublisher;
  }

  public Publisher<EmployeeFeedbackAdded> employeeFeedbackAdded() {
    return employeeFeedbackAddedPublisher.getPublisher();
  }
}
