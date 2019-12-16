package com.alarmcontrol.server.data.graphql.skill;

import com.alarmcontrol.server.data.graphql.ClientValidationException;
import com.alarmcontrol.server.data.models.Skill;
import com.alarmcontrol.server.data.repositories.EmployeeSkillRepository;
import com.alarmcontrol.server.data.repositories.SkillRepository;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class SkillMutations implements GraphQLMutationResolver {

  private SkillRepository skillRepository;
  private EmployeeSkillRepository employeeSkillRepository;

  public SkillMutations(SkillRepository skillRepository,
      EmployeeSkillRepository employeeSkillRepository) {
    this.skillRepository = skillRepository;
    this.employeeSkillRepository = employeeSkillRepository;
  }

  public Skill newSkill(Long organisationId, String name, String shortName, boolean displayAtOverview) {
    if (StringUtils.isBlank(name)) {
      throw new ClientValidationException("Name can not be blank");
    }

    if (StringUtils.isBlank(shortName)) {
      throw new ClientValidationException("ShortName can not be blank");
    }

    Skill skill = new Skill(organisationId, name, shortName, displayAtOverview);
    skillRepository.save(skill);
    return skill;
  }

  public Skill editSkill(Long id, String name, String shortName, boolean displayAtOverview) {
    Optional<Skill> skillById = skillRepository.findById(id);
    if (!skillById.isPresent()) {
      throw new ClientValidationException("No Skill found for id:" + id);
    }

    if (StringUtils.isBlank(name)) {
      throw new ClientValidationException("Name should not be blank");
    }

    if (StringUtils.isBlank(shortName)) {
      throw new ClientValidationException("ShortName should not be blank");
    }

    Skill skill = skillById.get();
    skill.setName(name);
    skill.setShortName(shortName);
    skill.setDisplayAtOverview(displayAtOverview);

    skillRepository.save(skill);
    return skill;
  }

  public Long deleteSkill(Long id) {
    Optional<Skill> skillById = skillRepository.findById(id);
    if (!skillById.isPresent()) {
      throw new ClientValidationException("No Skill found for id:" + id);
    }

    if (employeeSkillRepository.countBySkillId(id) > 0) {
      throw new ClientValidationException("skill is referenced by an employee");
    }

    Skill skill = skillById.get();
    skillRepository.delete(skill);
    return skill.getId();
  }
}
