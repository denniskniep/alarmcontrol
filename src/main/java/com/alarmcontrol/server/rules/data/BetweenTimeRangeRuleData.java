package com.alarmcontrol.server.rules.data;

import com.alarmcontrol.server.rules.BetweenTimeRangeRule;
import com.alarmcontrol.server.rules.Rule;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalTime;

public class BetweenTimeRangeRuleData implements RuleData{
    public final static String CLASSNAME = "BetweenTimeRangeRuleData";
    public final static String JSON_SUB_TYPE_NAME = "BetweenTimeRange";
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime from;
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime to;

    public BetweenTimeRangeRuleData() {
    }

    public LocalTime getTo() {
        return to;
    }

    public void setTo(LocalTime to) {
        this.to = to;
    }

    public LocalTime getFrom() {
        return from;
    }

    public void setFrom(LocalTime from) {
        this.from = from;
    }

    @Override
    public Rule create() {
        return new BetweenTimeRangeRule(this);
    }
}

