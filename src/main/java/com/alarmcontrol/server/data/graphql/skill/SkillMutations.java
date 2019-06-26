package com.alarmcontrol.server.data.graphql.skill;

import com.alarmcontrol.server.data.models.Skill;
import com.alarmcontrol.server.data.repositories.SkillRepository;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class SkillMutations implements GraphQLMutationResolver {

  private SkillRepository skillRepository;

  public SkillMutations(SkillRepository skillRepository) {
    this.skillRepository = skillRepository;
  }

  public Skill newSkill(Long organisationId, String name, String shortName, boolean displayAtOverview) {
    Skill skill = new Skill(organisationId, name, shortName, displayAtOverview);
    skillRepository.save(skill);
    return skill;
  }

  public Skill editSkill(Long id, String name, String shortName, boolean displayAtOverview) {
    Optional<Skill> skillById = skillRepository.findById(id);
    if (!skillById.isPresent()) {
      throw new RuntimeException("No Skill found for id:" + id);
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
      throw new RuntimeException("No Skill found for id:" + id);
    }

    Skill skill = skillById.get();
    skillRepository.delete(skill);
    return skill.getId();
  }
}
