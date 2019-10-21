package com.alarmcontrol.server.aaos;

import com.alarmcontrol.server.data.OrganisationConfigurationService;
import com.alarmcontrol.server.notifications.core.config.*;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class AaoConfigurationService {

    private OrganisationConfigurationService organisationConfigurationService;

    public AaoConfigurationService(OrganisationConfigurationService organisationConfigurationService) {
        this.organisationConfigurationService = organisationConfigurationService;
    }

    /*public NotificationSubscription addNotificationSubscription(Long organisationId,
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
    }*/

    public AaoBase addAao(Long organisationId, Aao aao){
        AaoOrganisationConfiguration config = organisationConfigurationService.loadAaoConfig(organisationId);

        List<AaoBase> aaoRules = config.getAaoRules();
        aaoRules.add(aao);
        config.setAaoRules(aaoRules);

        organisationConfigurationService.saveAaoConfig(organisationId, config);

        return aao;
    }
}
