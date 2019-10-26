package com.alarmcontrol.server.notifications.core.config;

import com.alarmcontrol.server.notifications.messaging.mail.MailContact;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY)
@JsonSubTypes({
    @JsonSubTypes.Type(value = MailContact.class, name = "MailContact")
})
public interface Contact {
  String getUniqueId();
  String getName();
}
