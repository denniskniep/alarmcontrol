package com.alarmcontrol.server.data.graphql.organisation;

import com.alarmcontrol.server.data.models.Organisation;
import com.alarmcontrol.server.data.repositories.OrganisationConfigurationRepository;
import com.alarmcontrol.server.rules.RuleService;
import com.alarmcontrol.server.rules.data.RuleContainerData;
import com.alarmcontrol.server.rules.data.RuleContainerDataCollection;
import com.coxautodev.graphql.tools.GraphQLResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class AaoRulesFieldAtOrganisationResolver implements GraphQLResolver<Organisation> {

  private OrganisationConfigurationRepository organisationConfigurationRepository;

  public AaoRulesFieldAtOrganisationResolver(
      OrganisationConfigurationRepository organisationConfigurationRepository) {
    this.organisationConfigurationRepository = organisationConfigurationRepository;
  }

  public ArrayList<RuleContainerData> aaoRules(Organisation organisation) {
    var organisationConfiguration = organisationConfigurationRepository.findByOrganisationIdAndKey(organisation.getId(), RuleService.AAO_KEY);
    if (organisationConfiguration.isEmpty()) {
      return null;
    }
    //var ruleContainerData = organisationConfiguration.get().deserialize();
    //return ruleContainerData;
    return null;
  }
}
