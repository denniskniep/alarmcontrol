package com.alarmcontrol.server.rules;

public interface AaoRule {
    MatchResult match(AlertContext alertContext);
}
