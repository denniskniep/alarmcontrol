package com.alarmcontrol.server.data.graphql.organisation;

import com.alarmcontrol.server.data.models.Organisation;
import com.alarmcontrol.server.data.models.Skill;
import com.alarmcontrol.server.data.repositories.SkillRepository;
import com.coxautodev.graphql.tools.GraphQLResolver;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SkillsFieldAtOrganisationResolver implements GraphQLResolver<Organisation> {

  private SkillRepository skillRepository;

  public SkillsFieldAtOrganisationResolver(SkillRepository skillRepository) {
    this.skillRepository = skillRepository;
  }

  public List<Skill> skills(Organisation organisation) {
    return skillRepository.findByOrganisationId(organisation.getId());
  }
}
