package com.alarmcontrol.server.notifications.core.config;

import com.alarmcontrol.server.notifications.usecases.alertcreated.AlertCreatedNotificationConfig;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY)
@JsonSubTypes({
    @JsonSubTypes.Type(value = AlertCreatedNotificationConfig.class, name = "AlertCreated")
})
public interface NotificationConfig {

}
