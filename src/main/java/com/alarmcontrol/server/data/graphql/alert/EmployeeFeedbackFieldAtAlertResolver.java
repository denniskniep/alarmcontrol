package com.alarmcontrol.server.data.graphql.alert;

import com.alarmcontrol.server.data.AlertEmployeeService;
import com.alarmcontrol.server.data.graphql.employeeFeedback.EmployeeFeedback;
import com.alarmcontrol.server.data.models.Alert;
import com.alarmcontrol.server.data.models.AlertCallEmployee;
import com.alarmcontrol.server.data.models.Feedback;
import com.alarmcontrol.server.data.repositories.EmployeeRepository;
import com.coxautodev.graphql.tools.GraphQLResolver;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class EmployeeFeedbackFieldAtAlertResolver implements GraphQLResolver<Alert> {

  private AlertEmployeeService alertEmployeeService;
  private EmployeeRepository employeeRepository;

  public EmployeeFeedbackFieldAtAlertResolver(AlertEmployeeService alertEmployeeService,
      EmployeeRepository employeeRepository) {
    this.alertEmployeeService = alertEmployeeService;
    this.employeeRepository = employeeRepository;
  }

  public List<EmployeeFeedback> employeeFeedback(Alert alert) {
    List<AlertCallEmployee> existingResponses = alertEmployeeService.findByAlertId(alert.getId());

    List<EmployeeFeedback> allEmployeesWithoutExistingResponses = employeeRepository
        .findByOrganisationId(alert.getOrganisationId())
        .stream()
        .map(e -> new EmployeeFeedback(e.getId(), Feedback.NO_RESPONSE, null))
        .filter(e -> !existsIn(existingResponses, e.getEmployeeId()))
        .collect(Collectors.toList());

    List<EmployeeFeedback> responses = new ArrayList<>();
    responses.addAll(allEmployeesWithoutExistingResponses);
    responses.addAll(existingResponses
        .stream()
        .map(e -> new EmployeeFeedback(e.getEmployeeId(), e.getFeedback(), e.getDateTime()))
        .collect(Collectors.toList()));
    return responses;
  }

  private boolean existsIn(List<AlertCallEmployee> existingResponses, Long employeeId) {
    return existingResponses.stream().anyMatch(e -> e.getEmployeeId().equals(employeeId));
  }
}
