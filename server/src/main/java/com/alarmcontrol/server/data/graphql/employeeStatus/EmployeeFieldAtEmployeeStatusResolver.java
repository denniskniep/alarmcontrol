package com.alarmcontrol.server.data.graphql.employeeStatus;

import com.alarmcontrol.server.data.models.Employee;
import com.alarmcontrol.server.data.models.EmployeeStatus;
import com.alarmcontrol.server.data.repositories.EmployeeRepository;
import com.coxautodev.graphql.tools.GraphQLResolver;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class EmployeeFieldAtEmployeeStatusResolver implements GraphQLResolver<EmployeeStatus> {

  private EmployeeRepository employeeRepository;

  public EmployeeFieldAtEmployeeStatusResolver(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  public Employee employee(EmployeeStatus employeeStatus) {
    Optional<Employee> employee = employeeRepository.findById(employeeStatus.getEmployeeId());
    if (employee.isPresent()) {
      return employee.get();
    }
    return null;
  }
}
