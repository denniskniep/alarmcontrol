package com.alarmcontrol.server.data.graphql.alert;

import com.alarmcontrol.server.data.EmployeeStatusService;
import com.alarmcontrol.server.data.models.Alert;
import com.alarmcontrol.server.data.models.Employee;
import com.alarmcontrol.server.data.models.EmployeeStatus;
import com.alarmcontrol.server.data.repositories.EmployeeRepository;
import com.coxautodev.graphql.tools.GraphQLResolver;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class EmployeeStatusFieldAtAlertResolver implements GraphQLResolver<Alert> {

  private EmployeeStatusService employeeStatusService;
  private EmployeeRepository employeeRepository;

  public EmployeeStatusFieldAtAlertResolver(EmployeeStatusService employeeStatusService,
      EmployeeRepository employeeRepository) {
    this.employeeStatusService = employeeStatusService;
    this.employeeRepository = employeeRepository;
  }

  public List<EmployeeStatus> employeeStatus(Alert alert) {
    List<Employee> employees = employeeRepository.findByOrganisationId(alert.getOrganisationId());
    return employees
        .stream()
        .map(e -> employeeStatusService.getEmployeeStatusUntilOrEqual(e.getId(), alert.getUtcDateTime()))
        .filter(s -> s != null)
        .collect(Collectors.toList());
  }
}
