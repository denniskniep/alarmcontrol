package com.alarmcontrol.server.data.graphql.alert;

import com.alarmcontrol.server.data.models.Alert;
import com.alarmcontrol.server.data.models.AlertEmployee;
import com.alarmcontrol.server.data.models.AlertEmployee.Feedback;
import com.alarmcontrol.server.data.repositories.AlertEmployeeRepository;
import com.alarmcontrol.server.data.repositories.EmployeeRepository;
import com.coxautodev.graphql.tools.GraphQLResolver;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class EmployeeFeedbackFieldAtAlertResolver implements GraphQLResolver<Alert> {

  private AlertEmployeeRepository alertEmployeeRepository;
  private EmployeeRepository employeeRepository;

  public EmployeeFeedbackFieldAtAlertResolver(AlertEmployeeRepository alertEmployeeRepository,
      EmployeeRepository employeeRepository) {
    this.alertEmployeeRepository = alertEmployeeRepository;
    this.employeeRepository = employeeRepository;
  }

  public List<AlertEmployee> employeeFeedback(Alert alert) {
    List<AlertEmployee> existingResponses = alertEmployeeRepository.findByAlertId(alert.getId());

    List<AlertEmployee> allEmployeesWithoutExistingResponses = employeeRepository
        .findByOrganisationId(alert.getOrganisationId())
        .stream()
        .map(e -> new AlertEmployee(e.getId(), alert.getId(), Feedback.NO_RESPONSE, null))
        .filter(e-> !existsIn(existingResponses, e.getEmployeeId()))
        .collect(Collectors.toList());

    List<AlertEmployee> responses = new ArrayList<>();
    responses.addAll(allEmployeesWithoutExistingResponses);
    responses.addAll(existingResponses);
    return responses;
  }

  private boolean existsIn(List<AlertEmployee> existingResponses, Long employeeId) {
    return existingResponses.stream().anyMatch( e -> e.getEmployeeId().equals(employeeId));
  }
}
