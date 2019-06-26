package com.alarmcontrol.server.data.graphql.alert;

import com.alarmcontrol.server.data.EmployeeFeedbackService;
import com.alarmcontrol.server.data.graphql.employeeFeedback.EmployeeFeedback;
import com.alarmcontrol.server.data.models.Alert;
import com.alarmcontrol.server.data.repositories.EmployeeRepository;
import com.coxautodev.graphql.tools.GraphQLResolver;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class EmployeeFeedbackFieldAtAlertResolver implements GraphQLResolver<Alert> {

  private EmployeeFeedbackService employeeFeedbackService;

  public EmployeeFeedbackFieldAtAlertResolver(EmployeeFeedbackService employeeFeedbackService) {
    this.employeeFeedbackService = employeeFeedbackService;
  }

  public List<EmployeeFeedback> employeeFeedback(Alert alert) {
    return employeeFeedbackService.findByAlertId(alert.getId());
  }
}
