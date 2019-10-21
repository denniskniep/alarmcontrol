package com.alarmcontrol.server.aaos;

import com.alarmcontrol.server.aaos.AlertCreatedAaoConfig;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AlertCreatedAaoConfig.class, name = "AlertCreated")
})
public interface AaoConfig {

}

