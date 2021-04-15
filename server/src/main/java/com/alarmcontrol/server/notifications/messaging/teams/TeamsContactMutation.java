package com.alarmcontrol.server.notifications.messaging.teams;

import com.alarmcontrol.server.notifications.core.NotificationConfigurationService;
import com.alarmcontrol.server.notifications.core.config.Contact;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TeamsContactMutation implements GraphQLMutationResolver {

  private NotificationConfigurationService notificationConfigurationService;

  public TeamsContactMutation(NotificationConfigurationService notificationConfigurationService) {
    this.notificationConfigurationService = notificationConfigurationService;
  }

  public Contact addNotificationTeamsContact(Long organisationId, String name, String url) {
    TeamsContact teamsContact = new TeamsContact();
    teamsContact.setUniqueId(UUID.randomUUID().toString());
    teamsContact.setName(name);
    teamsContact.setUrl(url);
    return notificationConfigurationService.addNotificationContact(organisationId, teamsContact);
  }
}
