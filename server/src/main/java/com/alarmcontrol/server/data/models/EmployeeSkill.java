package com.alarmcontrol.server.data.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class EmployeeSkill {

  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  private Long employeeId;

  private Long skillId;

  protected EmployeeSkill() {}

  public EmployeeSkill(Long employeeId, Long skillId) {
    this.employeeId = employeeId;
    this.skillId = skillId;
  }

  public Long getId() {
    return id;
  }

  public Long getEmployeeId() {
    return employeeId;
  }

  public Long getSkillId() {
    return skillId;
  }
}
