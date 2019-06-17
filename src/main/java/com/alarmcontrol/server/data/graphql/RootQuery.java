package com.alarmcontrol.server.data.graphql;

import com.alarmcontrol.server.data.models.Alert;
import com.alarmcontrol.server.data.models.Organisation;
import com.alarmcontrol.server.data.models.Skill;
import com.alarmcontrol.server.data.repositories.AlertRepository;
import com.alarmcontrol.server.data.repositories.OrganisationRepository;
import com.alarmcontrol.server.data.repositories.SkillRepository;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class RootQuery implements GraphQLQueryResolver {
  private AlertRepository alertRepository;
  private SkillRepository skillRepository;
  private OrganisationRepository organisationRepository;

  public RootQuery(AlertRepository alertRepository,
      SkillRepository skillRepository,
      OrganisationRepository organisationRepository) {
    this.alertRepository = alertRepository;
    this.skillRepository = skillRepository;
    this.organisationRepository = organisationRepository;
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

  public Iterable<Organisation> organisations() {
    return organisationRepository.findAll();
  }

  public Organisation organisationById(Long organisationId) {
    Optional<Organisation> organisation = organisationRepository.findById(organisationId);
    if(organisation.isPresent()){
      return organisation.get();
    }
    return null;
  }

}