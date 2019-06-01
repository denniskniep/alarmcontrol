package com.alarmcontrol.server.data.graphql.alert;

import com.alarmcontrol.server.data.models.AlertEmployee;
import com.alarmcontrol.server.data.models.Employee;
import com.alarmcontrol.server.data.repositories.EmployeeRepository;
import com.coxautodev.graphql.tools.GraphQLResolver;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class EmployeeFieldAtAlertEmployeeResolver implements GraphQLResolver<AlertEmployee> {

  private EmployeeRepository employeeRepository;

  public EmployeeFieldAtAlertEmployeeResolver(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  public Employee employee(AlertEmployee alertEmployee) {
    Optional<Employee> employee = employeeRepository.findById(alertEmployee.getEmployeeId());
    if(employee.isPresent()){
      return employee.get();
    }
    return null;
  }
}
