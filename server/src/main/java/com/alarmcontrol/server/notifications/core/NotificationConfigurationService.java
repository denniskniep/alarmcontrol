package com.alarmcontrol.server.notifications.core;

import com.alarmcontrol.server.data.OrganisationConfigurationService;
import com.alarmcontrol.server.notifications.core.config.Contact;
import com.alarmcontrol.server.notifications.core.config.NotificationConfig;
import com.alarmcontrol.server.notifications.core.config.NotificationOrganisationConfiguration;
import com.alarmcontrol.server.notifications.core.config.NotificationSubscription;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class NotificationConfigurationService {

    private OrganisationConfigurationService organisationConfigurationService;

    public NotificationConfigurationService(OrganisationConfigurationService organisationConfigurationService) {
      this.organisationConfigurationService = organisationConfigurationService;
    }

    public NotificationSubscription addNotificationSubscription(Long organisationId,
        List<String> subscriberContactUniqueIds, NotificationConfig config) {
      NotificationOrganisationConfiguration notificationOrganisationConfiguration = organisationConfigurationService
          .loadNotificationConfig(organisationId);

      List<NotificationSubscription> subscriptions = notificationOrganisationConfiguration.getSubscriptions();

      NotificationSubscription subscription = new NotificationSubscription();
      subscription.setUniqueId(UUID.randomUUID().toString());
      subscription.setSubscriberContactUniqueIds(subscriberContactUniqueIds);
      subscription.setNotificationConfig(config);

      subscriptions.add(subscription);
      notificationOrganisationConfiguration.setSubscriptions(subscriptions);

      organisationConfigurationService.saveNotificationConfig(organisationId, notificationOrganisationConfiguration);
      return subscription;
    }

    public NotificationSubscription editNotificationSubscription(Long organisationId,
        String uniqueSubscriptionId,
        List<String> subscriberContactUniqueIds,
        NotificationConfig config) {
      NotificationOrganisationConfiguration notificationOrganisationConfiguration = organisationConfigurationService
          .loadNotificationConfig(organisationId);

      List<NotificationSubscription> subscriptions = notificationOrganisationConfiguration.getSubscriptions();

      List<NotificationSubscription> subscriptionsToEdit = subscriptions
          .stream()
          .filter(s -> StringUtils.equals(s.getUniqueId(), uniqueSubscriptionId))
          .collect(Collectors.toList());

      if (subscriptionsToEdit.isEmpty()) {
        throw new RuntimeException("No Subscription found with id " + uniqueSubscriptionId);
      }

      if (subscriptionsToEdit.size() > 1) {
        throw new RuntimeException("Too many Subscription found with id " + uniqueSubscriptionId);
      }

      NotificationSubscription subscription = subscriptionsToEdit.get(0);
      subscription.setSubscriberContactUniqueIds(subscriberContactUniqueIds);
      subscription.setNotificationConfig(config);

      organisationConfigurationService.saveNotificationConfig(organisationId, notificationOrganisationConfiguration);

      return subscription;
    }

    public Contact addNotificationContact(Long organisationId, Contact contact){
      NotificationOrganisationConfiguration config = organisationConfigurationService.loadNotificationConfig(organisationId);

      List<Contact> contacts = config.getContacts();
      contacts.add(contact);
      config.setContacts(contacts);

      organisationConfigurationService.saveNotificationConfig(organisationId, config);

      return contact;
    }
  }
