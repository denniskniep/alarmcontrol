package com.alarmcontrol.server.data.graphql.organisation;

import com.alarmcontrol.server.data.OrganisationConfigurationService;
import com.alarmcontrol.server.data.models.Organisation;
import com.alarmcontrol.server.notifications.core.config.AaoOrganisationConfiguration;
import com.coxautodev.graphql.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

@Component
public class AaoConfigAtOrganisationResolver implements GraphQLResolver<Organisation> {

  private OrganisationConfigurationService organisationConfigurationService;

  public AaoConfigAtOrganisationResolver(
      OrganisationConfigurationService organisationConfigurationService) {
    this.organisationConfigurationService = organisationConfigurationService;
  }

  public AaoOrganisationConfiguration aaoConfig(Organisation organisation) {
    return organisationConfigurationService.loadAaoConfig(organisation.getId());
  }
}