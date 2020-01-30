package com.alarmcontrol.server.data.graphql.organisation;

import com.alarmcontrol.server.data.OrganisationConfigurationService;
import com.alarmcontrol.server.data.models.Organisation;
import com.alarmcontrol.server.notifications.core.config.NotificationOrganisationConfiguration;
import com.coxautodev.graphql.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

@Component
public class NotificationConfigAtOrganisationResolver  implements GraphQLResolver<Organisation> {

  private OrganisationConfigurationService organisationConfigurationService;

  public NotificationConfigAtOrganisationResolver(
      OrganisationConfigurationService organisationConfigurationService) {
    this.organisationConfigurationService = organisationConfigurationService;
  }

  public NotificationOrganisationConfiguration notificationConfig(Organisation organisation) {
    return organisationConfigurationService.loadNotificationConfig(organisation.getId());
  }
}