package com.alarmcontrol.server.data.graphql.employeeFeedback;

import com.alarmcontrol.server.data.models.AlertCallEmployee;
import com.alarmcontrol.server.data.models.Employee;
import com.alarmcontrol.server.data.repositories.EmployeeRepository;
import com.coxautodev.graphql.tools.GraphQLResolver;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class EmployeeFieldAtEmployeeFeedbackResolver implements GraphQLResolver<EmployeeFeedback> {

  private EmployeeRepository employeeRepository;

  public EmployeeFieldAtEmployeeFeedbackResolver(EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  public Employee employee(EmployeeFeedback employeeFeedback) {
    Optional<Employee> employee = employeeRepository.findById(employeeFeedback.getEmployeeId());
    if(employee.isPresent()){
      return employee.get();
    }
    return null;
  }
}
