package com.alarmcontrol.server.data.graphql.employee;

import com.alarmcontrol.server.data.EmployeeStatusService;
import com.alarmcontrol.server.data.models.Employee;
import com.alarmcontrol.server.data.models.EmployeeStatus;
import com.alarmcontrol.server.data.repositories.EmployeeStatusRepository;
import com.coxautodev.graphql.tools.GraphQLResolver;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class StatusFieldAtEmployeeResolver implements GraphQLResolver<Employee> {

  private EmployeeStatusService employeeStatusService;

  public StatusFieldAtEmployeeResolver(EmployeeStatusService employeeStatusService) {
    this.employeeStatusService = employeeStatusService;
  }

  public EmployeeStatus status(Employee employee) {
    return employeeStatusService.getEmployeeStatus(employee.getId());
  }
}
