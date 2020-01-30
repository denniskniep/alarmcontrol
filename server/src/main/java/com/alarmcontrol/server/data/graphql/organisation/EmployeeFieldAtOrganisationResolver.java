package com.alarmcontrol.server.data.graphql.organisation;

import com.alarmcontrol.server.data.models.Employee;
import com.alarmcontrol.server.data.models.Organisation;
import com.alarmcontrol.server.data.repositories.EmployeeRepository;
import com.coxautodev.graphql.tools.GraphQLResolver;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class EmployeeFieldAtOrganisationResolver implements GraphQLResolver<Organisation> {

  private EmployeeRepository employeeRepository;

  public EmployeeFieldAtOrganisationResolver(
      EmployeeRepository employeeRepository) {
    this.employeeRepository = employeeRepository;
  }

  public List<Employee> employees(Organisation organisation) {
    return employeeRepository
        .findByOrganisationId(organisation.getId());
  }
}
