package com.alarmcontrol.server.data.graphql.alertCall;

import com.alarmcontrol.server.data.graphql.employeeFeedback.EmployeeFeedback;
import com.alarmcontrol.server.data.models.AlertCall;
import com.alarmcontrol.server.data.repositories.AlertCallEmployeeRepository;
import com.coxautodev.graphql.tools.GraphQLResolver;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class EmployeeFeedbackFieldAtAlertCallResolver implements GraphQLResolver<AlertCall> {

  private AlertCallEmployeeRepository alertCallEmployeeRepository;

  public EmployeeFeedbackFieldAtAlertCallResolver(
      AlertCallEmployeeRepository alertCallEmployeeRepository) {
    this.alertCallEmployeeRepository = alertCallEmployeeRepository;
  }

  public List<EmployeeFeedback> employeeFeedback(AlertCall alertCall) {
    return alertCallEmployeeRepository.findByAlertCallId(alertCall.getId())
        .stream()
        .map(c -> new EmployeeFeedback(c.getEmployeeId(), c.getFeedback(), c.getDateTime()))
        .collect(Collectors.toList());
  }
}
