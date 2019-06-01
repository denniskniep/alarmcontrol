package com.alarmcontrol.server.data.graphql;

import com.alarmcontrol.server.data.models.Alert;
import com.alarmcontrol.server.data.models.Skill;
import com.alarmcontrol.server.data.repositories.AlertRepository;
import com.alarmcontrol.server.data.repositories.SkillRepository;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class RootQuery implements GraphQLQueryResolver {
  private AlertRepository alertRepository;
  private SkillRepository skillRepository;

  public RootQuery(AlertRepository alertRepository,
      SkillRepository skillRepository) {
    this.alertRepository = alertRepository;
    this.skillRepository = skillRepository;
  }

  public Alert alertById(Long id) {
    Optional<Alert> alert = alertRepository.findById(id);
    if(alert.isPresent()){
      return alert.get();
    }
    return null;
  }

  public Iterable<Alert> alertsByOrganisationId(Long organisationId) {
    return alertRepository.findByOrganisationId(organisationId);
  }

  public Iterable<Skill> skillsByOrganisationId(Long organisationId) {
    return skillRepository.findByOrganisationId(organisationId);
  }
}