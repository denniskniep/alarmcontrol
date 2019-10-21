package com.alarmcontrol.server.notifications.core.config;

import com.alarmcontrol.server.notifications.messaging.mail.MailContact;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = com.alarmcontrol.server.aaos.Aao.class, name = "AaoRule")
})
public interface AaoBase {
  String getUniqueId();
  String getName();
}
