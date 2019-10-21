package com.alarmcontrol.server.notifications.core.config;

import java.util.ArrayList;
import java.util.List;

public class NotificationOrganisationConfiguration {

  public static final String KEY = "NOTIFICATION";

  private List<Contact> contacts;

  private List<NotificationSubscription> subscriptions;

  public NotificationOrganisationConfiguration() {
    contacts = new ArrayList<>();
    subscriptions = new ArrayList<>();
  }

  public List<Contact> getContacts() {
    return contacts;
  }

  public void setContacts(List<Contact> contacts) {
    this.contacts = contacts;
  }

  public List<NotificationSubscription> getSubscriptions() {
    return subscriptions;
  }

  public void setSubscriptions(
      List<NotificationSubscription> subscriptions) {
    this.subscriptions = subscriptions;
  }
}

