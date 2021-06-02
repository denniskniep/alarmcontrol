package com.alarmcontrol.server.notifications.core.config;

import com.alarmcontrol.server.notifications.messaging.firebasepush.FirebaseMessageContact;
import com.alarmcontrol.server.notifications.messaging.mail.MailContact;
import com.alarmcontrol.server.notifications.messaging.teams.TeamsContact;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY)
@JsonSubTypes({
    @JsonSubTypes.Type(value = MailContact.class, name = "MailContact"),
    @JsonSubTypes.Type(value = TeamsContact.class, name = "TeamsContact"),
    @JsonSubTypes.Type(value = FirebaseMessageContact.class, name = "FirebaseMessageContact")
})
public interface Contact {
  String getUniqueId();
  String getName();
}
