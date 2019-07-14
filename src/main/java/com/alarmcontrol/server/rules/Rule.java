package com.alarmcontrol.server.rules;

public interface Rule {
    boolean match(AlertContext alertContext);
}

