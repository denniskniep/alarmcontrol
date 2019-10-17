package com.alarmcontrol.server.notifications.usecases.alertcreated;

import com.alarmcontrol.server.notifications.core.NotificationConfigurationService;
import com.alarmcontrol.server.notifications.core.config.NotificationSubscription;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class AlertCreatedNotificationConfigurationMutation implements GraphQLMutationResolver {

  private NotificationConfigurationService notificationConfigurationService;

  public AlertCreatedNotificationConfigurationMutation(
      NotificationConfigurationService notificationConfigurationService) {
    this.notificationConfigurationService = notificationConfigurationService;
  }

  public NotificationSubscription addNotificationSubscriptionForAlertCreated(Long organisationId,
      List<Integer> updateDelaysInSeconds,
      List<String> subscriberContactUniqueIds) {

    AlertCreatedNotificationConfig config = new AlertCreatedNotificationConfig();
    config.setUpdateDelaysInSeconds(updateDelaysInSeconds);

    return notificationConfigurationService
        .addNotificationSubscription(organisationId, subscriberContactUniqueIds, config);
  }

  public NotificationSubscription editNotificationSubscriptionForAlertCreated(Long organisationId,
      String uniqueSubscriptionId,
      List<Integer> updateDelaysInSeconds,
      List<String> subscriberContactUniqueIds) {

    AlertCreatedNotificationConfig config = new AlertCreatedNotificationConfig();
    config.setUpdateDelaysInSeconds(updateDelaysInSeconds);

    return notificationConfigurationService
        .editNotificationSubscription(organisationId, uniqueSubscriptionId, subscriberContactUniqueIds, config);
  }
}
