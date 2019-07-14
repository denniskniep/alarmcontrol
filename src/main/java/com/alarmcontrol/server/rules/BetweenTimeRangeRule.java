package com.alarmcontrol.server.rules;

import com.alarmcontrol.server.rules.data.BetweenTimeRangeRuleData;

import java.time.LocalTime;

public class BetweenTimeRangeRule implements Rule {
    private LocalTime from;
    private LocalTime to;

    public BetweenTimeRangeRule(LocalTime from, LocalTime to) {
        this.from = from;
        this.to = to;
    }

    public BetweenTimeRangeRule(BetweenTimeRangeRuleData betweenTimeRangeRuleData) {
        this(betweenTimeRangeRuleData.getFrom(), betweenTimeRangeRuleData.getTo());
    }

    @Override
    public boolean match(AlertContext alertContext) {
        var alertTime = alertContext.getLocalTime();
        var isInRange = this.from.compareTo(alertTime) == 0 ||
                this.to.compareTo(alertTime) == 0 ||
                this.from.isBefore(alertTime) && this.to.isAfter(alertTime);

        return isInRange;
    }

}

