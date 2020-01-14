package com.alarmcontrol.server.data.graphql.employee;

import com.alarmcontrol.server.data.models.Employee;
import com.alarmcontrol.server.data.models.Skill;
import com.alarmcontrol.server.data.repositories.EmployeeSkillRepository;
import com.alarmcontrol.server.data.repositories.SkillRepository;
import com.coxautodev.graphql.tools.GraphQLResolver;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class SkillFieldAtEmployeeResolver implements GraphQLResolver<Employee> {

  private EmployeeSkillRepository employeeSkillRepository;
  private SkillRepository skillRepository;

  public SkillFieldAtEmployeeResolver(
      EmployeeSkillRepository employeeSkillRepository,
      SkillRepository skillRepository) {
    this.employeeSkillRepository = employeeSkillRepository;
    this.skillRepository = skillRepository;
  }

  public List<Skill> skills(Employee employee) {

    //TODO: Refactor to INNER JOIN
    List<Long> skillIds = employeeSkillRepository
        .findByEmployeeId(employee.getId())
        .stream()
        .map(es -> es.getSkillId())
        .collect(Collectors.toList());

    return Lists.newArrayList(skillRepository.findAllById(skillIds));
  }
}
