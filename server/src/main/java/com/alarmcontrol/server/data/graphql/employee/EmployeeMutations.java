package com.alarmcontrol.server.data.graphql.employee;

import com.alarmcontrol.server.data.graphql.ClientValidationException;
import com.alarmcontrol.server.data.models.Employee;
import com.alarmcontrol.server.data.models.EmployeeSkill;
import com.alarmcontrol.server.data.repositories.EmployeeRepository;
import com.alarmcontrol.server.data.repositories.EmployeeSkillRepository;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMutations implements GraphQLMutationResolver {

  private EmployeeRepository employeeRepository;
  private EmployeeSkillRepository employeeSkillRepository;

  public EmployeeMutations(EmployeeRepository employeeRepository,
      EmployeeSkillRepository employeeSkillRepository) {
    this.employeeRepository = employeeRepository;
    this.employeeSkillRepository = employeeSkillRepository;
  }

  public Employee newEmployee(Long organisationId,
      String firstname,
      String lastname,
      String referenceId) {

    if (StringUtils.isBlank(firstname)) {
      throw new ClientValidationException("Firstname should not be blank");
    }

    if (StringUtils.isBlank(lastname)) {
      throw new ClientValidationException("Lastname should not be blank");
    }

    if (StringUtils.isBlank(referenceId)) {
      throw new ClientValidationException("ReferenceId should not be blank");
    }

    Employee employee = new Employee(organisationId, firstname, lastname, referenceId);
    return employeeRepository.save(employee);
  }

  public Long deleteEmployee(Long id) {
    Optional<Employee> employeeById = employeeRepository.findById(id);
    if (!employeeById.isPresent()) {
      throw new ClientValidationException("No Employee found for id:" + id);
    }

    List<EmployeeSkill> employeeSkills = employeeSkillRepository
        .findByEmployeeId(id);
    employeeSkillRepository.deleteAll(employeeSkills);

    Employee employee = employeeById.get();
    employeeRepository.delete(employee);
    return employee.getId();
  }

  public Employee editEmployee(Long id,
      String firstname,
      String lastname,
      String referenceId) {

    Optional<Employee> employeeById = employeeRepository.findById(id);
    if (!employeeById.isPresent()) {
      throw new ClientValidationException("No Employee found for id:" + id);
    }

    if (StringUtils.isBlank(firstname)) {
      throw new ClientValidationException("Firstname should not be blank");
    }

    if (StringUtils.isBlank(lastname)) {
      throw new ClientValidationException("Lastname should not be blank");
    }

    if (StringUtils.isBlank(referenceId)) {
      throw new ClientValidationException("ReferenceId should not be blank");
    }

    Employee employee = employeeById.get();
    employee.setFirstname(firstname);
    employee.setLastname(lastname);
    employee.setReferenceId(referenceId);

    employeeRepository.save(employee);
    return employee;
  }

  public boolean addEmployeeSkill(Long employeeId, Long skillId) {
    List<EmployeeSkill> existingEmployeeSkill = employeeSkillRepository
        .findByEmployeeIdAndSkillId(employeeId, skillId);

    if (existingEmployeeSkill.size() == 0) {
      employeeSkillRepository.save(new EmployeeSkill(employeeId, skillId));
      return true;
    }
    return false;
  }

  public boolean deleteEmployeeSkill(Long employeeId, Long skillId) {
    List<EmployeeSkill> existingEmployeeSkill = employeeSkillRepository
        .findByEmployeeIdAndSkillId(employeeId, skillId);

    if (existingEmployeeSkill.size() != 0) {
      employeeSkillRepository.deleteAll(existingEmployeeSkill);
      return true;
    }
    return false;
  }
}
