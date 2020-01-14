package com.alarmcontrol.server.notifications.messaging.mail;

import com.alarmcontrol.server.notifications.core.NotificationConfigurationService;
import com.alarmcontrol.server.notifications.core.config.Contact;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class MailContactMutation implements GraphQLMutationResolver {

  private NotificationConfigurationService notificationConfigurationService;

  public MailContactMutation(NotificationConfigurationService notificationConfigurationService) {
    this.notificationConfigurationService = notificationConfigurationService;
  }

  public Contact addNotificationMailContact(Long organisationId, String name, String mailAddress) {
    MailContact mailContact = new MailContact();
    mailContact.setUniqueId(UUID.randomUUID().toString());
    mailContact.setName(name);
    mailContact.setMailAddress(mailAddress);
    return notificationConfigurationService.addNotificationContact(organisationId, mailContact);
  }
}
