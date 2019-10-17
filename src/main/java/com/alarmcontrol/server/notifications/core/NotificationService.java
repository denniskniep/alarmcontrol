package com.alarmcontrol.server.notifications.core;

import com.alarmcontrol.server.data.OrganisationConfigurationService;
import com.alarmcontrol.server.notifications.core.config.NotificationConfig;
import com.alarmcontrol.server.notifications.core.config.NotificationOrganisationConfiguration;
import com.alarmcontrol.server.notifications.core.config.NotificationSubscription;
import com.alarmcontrol.server.notifications.core.config.Contact;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class NotificationService implements ApplicationContextAware {

  private Logger logger = LoggerFactory
      .getLogger(NotificationService.class);

  private ApplicationContext applicationContext;
  private OrganisationConfigurationService organisationConfigurationService;

  public NotificationService(OrganisationConfigurationService organisationConfigurationService) {
    this.organisationConfigurationService = organisationConfigurationService;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  public void sendNotifications(Event event) {
    Class<? extends NotificationBuilder> notificationBuilderClass = event.getNotificationBuilderClass();
    NotificationBuilder notificationBuilder = applicationContext.getBean(notificationBuilderClass);

    if(notificationBuilder == null){
      logger.error("No notification builder found for event '{}' with class name '{}'",
          event.getClass().getSimpleName(),
          notificationBuilderClass.getSimpleName());
      return;
    }

    List<ResolvedNotificationSubscription> subscriptions = findSubscriptions(
        event.getOrganisationId(),
        notificationBuilder.getNotificationConfigClass());

    for (ResolvedNotificationSubscription subscription : subscriptions) {
      notificationBuilder.sendNotifications(event, subscription.getNotificationConfig(), subscription.getContacts());
    }
  }

  private List<ResolvedNotificationSubscription> findSubscriptions(long organisationId, Class<? extends NotificationConfig> configClass){
    NotificationOrganisationConfiguration notificationConfig = organisationConfigurationService
        .loadNotificationConfig(organisationId);

    if (notificationConfig == null) {
      logger.info("No NotificationConfig found for OrganisationId {}!", organisationId);
      return new ArrayList<>();
    }

    List<NotificationSubscription> subscriptions = notificationConfig
        .getSubscriptions()
        .stream()
        .filter(s -> s.getNotificationConfig() != null &&
            s.getNotificationConfig().getClass().equals(configClass))
        .collect(Collectors.toList());

    if (subscriptions.isEmpty()) {
      logger.info("No Subscription found for OrganisationId {} with Config {}!",
          organisationId,
          configClass.getSimpleName());
    }

    List<ResolvedNotificationSubscription> resolvedNotificationSubscriptions = new ArrayList<>();
    for (NotificationSubscription subscription : subscriptions) {
      List<Contact> subscribedContacts = filterContacts(notificationConfig.getContacts(),
          subscription.getSubscriberContactUniqueIds());

      ResolvedNotificationSubscription resolvedNotificationSubscription = new ResolvedNotificationSubscription(
          subscription.getNotificationConfig(),
          subscribedContacts);

      resolvedNotificationSubscriptions.add(resolvedNotificationSubscription);
    }
    return resolvedNotificationSubscriptions;
  }

  private List<Contact> filterContacts(List<Contact> allContacts, List<String> contactUniqueIds) {
    List<Contact> contacts = new ArrayList<>();
    for (String contactUniqueId : contactUniqueIds) {
      Optional<Contact> foundContact = allContacts
          .stream()
          .filter(c -> StringUtils.equals(c.getUniqueId(),contactUniqueId))
          .findAny();

      if (foundContact.isEmpty()) {
        logger.warn("No Contact found with uniqueId:'{}' in NotificationConfig", contactUniqueId);
      }
      contacts.add(foundContact.get());
    }
    return contacts;
  }

  private class ResolvedNotificationSubscription{
    private NotificationConfig notificationConfig;
    private List<Contact> contacts;

    public ResolvedNotificationSubscription(
        NotificationConfig notificationConfig,
        List<Contact> contacts) {
      this.notificationConfig = notificationConfig;
      this.contacts = contacts;
    }

    public NotificationConfig getNotificationConfig() {
      return notificationConfig;
    }

    public List<Contact> getContacts() {
      return contacts;
    }
  }
}
