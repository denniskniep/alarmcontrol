package com.alarmcontrol.server.notifications.messaging.firebasepush;

import com.alarmcontrol.server.notifications.core.NotificationConfigurationService;
import com.alarmcontrol.server.notifications.core.config.Contact;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class FirebaseMessageMutation implements GraphQLMutationResolver {

  private NotificationConfigurationService notificationConfigurationService;

  public FirebaseMessageMutation(NotificationConfigurationService notificationConfigurationService) {
    this.notificationConfigurationService = notificationConfigurationService;
  }

  public Contact addNotificationFirebaseMessageContact(Long organisationId, String name, String mail) {
    FirebaseMessageContact contact = new FirebaseMessageContact();
    contact.setUniqueId(UUID.randomUUID().toString());
    contact.setName(name);
    contact.setMail(mail);
    return notificationConfigurationService.addNotificationContact(organisationId, contact);
  }
}
