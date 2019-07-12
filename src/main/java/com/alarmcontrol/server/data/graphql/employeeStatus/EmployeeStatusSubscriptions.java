package com.alarmcontrol.server.data.graphql.employeeStatus;

import com.alarmcontrol.server.data.graphql.employeeStatus.publisher.EmployeeStatusAdded;
import com.alarmcontrol.server.data.graphql.employeeStatus.publisher.EmployeeStatusAddedPublisher;
import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;

@Component
public class EmployeeStatusSubscriptions implements GraphQLSubscriptionResolver {
  private EmployeeStatusAddedPublisher employeeStatusAddedPublisher;

  public EmployeeStatusSubscriptions(
      EmployeeStatusAddedPublisher employeeStatusAddedPublisher) {
    this.employeeStatusAddedPublisher = employeeStatusAddedPublisher;
  }

  public Publisher<EmployeeStatusAdded> employeeStatusAdded() {
    return employeeStatusAddedPublisher.getPublisher();
  }
}
