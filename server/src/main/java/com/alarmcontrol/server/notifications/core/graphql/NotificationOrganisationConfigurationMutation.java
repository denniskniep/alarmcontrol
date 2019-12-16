package com.alarmcontrol.server.notifications.core.graphql;

import com.alarmcontrol.server.data.OrganisationConfigurationService;
import com.alarmcontrol.server.notifications.core.config.NotificationOrganisationConfiguration;
import com.alarmcontrol.server.notifications.core.config.NotificationSubscription;
import com.alarmcontrol.server.notifications.core.config.Contact;
import com.alarmcontrol.server.notifications.messaging.mail.MailContact;
import com.alarmcontrol.server.notifications.usecases.alertcreated.AlertCreatedNotificationConfig;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class NotificationOrganisationConfigurationMutation implements GraphQLMutationResolver {

  private OrganisationConfigurationService organisationConfigurationService;

  public NotificationOrganisationConfigurationMutation(
      OrganisationConfigurationService organisationConfigurationService) {
    this.organisationConfigurationService = organisationConfigurationService;
  }

  public String deleteNotificationContact(Long organisationId, String uniqueContactId) {
    NotificationOrganisationConfiguration config = load(organisationId);

    // TODO: Remove from all Subscriptions!

    List<Contact> contacts = config.getContacts();

    List<Contact> contactsToDelete = contacts
        .stream()
        .filter(c -> StringUtils.equals(c.getUniqueId(), uniqueContactId))
        .collect(Collectors.toList());

    if (contactsToDelete.isEmpty()) {
      throw new RuntimeException("No Contact found with id " + uniqueContactId);
    }

    for (Contact contact : contactsToDelete) {
      contacts.remove(contact);
    }

    config.setContacts(contacts);
    organisationConfigurationService.saveNotificationConfig(organisationId, config);

    return uniqueContactId;
  }

  public String deleteNotificationSubscription(Long organisationId, String uniqueSubscriptionId){
    NotificationOrganisationConfiguration config = load(organisationId);

    List<NotificationSubscription> subscriptions = config.getSubscriptions();

    List<NotificationSubscription> subscriptionsToDelete = subscriptions
        .stream()
        .filter(s -> StringUtils.equals(s.getUniqueId(), uniqueSubscriptionId))
        .collect(Collectors.toList());

    if (subscriptionsToDelete.isEmpty()) {
      throw new RuntimeException("No Subscription found with id " + uniqueSubscriptionId);
    }

    for (NotificationSubscription notificationSubscription : subscriptionsToDelete) {
      subscriptions.remove(notificationSubscription);
    }

    config.setSubscriptions(subscriptions);
    organisationConfigurationService.saveNotificationConfig(organisationId, config);

    return uniqueSubscriptionId;
  }


  private NotificationOrganisationConfiguration load(Long organisationId) {
    return organisationConfigurationService
        .loadNotificationConfig(organisationId);
  }
}
