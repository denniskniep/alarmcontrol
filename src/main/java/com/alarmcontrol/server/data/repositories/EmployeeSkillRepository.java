package com.alarmcontrol.server.data.repositories;

import com.alarmcontrol.server.data.models.EmployeeSkill;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeSkillRepository extends CrudRepository<EmployeeSkill, Long> {
  List<EmployeeSkill> findByEmployeeIdAndSkillId(Long employeeId, Long skillId);
  List<EmployeeSkill> findByEmployeeId(Long employeeId);
  long countBySkillId(Long skillId);
}