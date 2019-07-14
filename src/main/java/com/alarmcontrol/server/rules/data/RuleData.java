package com.alarmcontrol.server.rules.data;

import com.alarmcontrol.server.rules.Rule;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = BetweenTimeRangeRuleData.class, name = BetweenTimeRangeRuleData.JSON_SUB_TYPE_NAME)})
public interface RuleData {
    Rule create();
}

