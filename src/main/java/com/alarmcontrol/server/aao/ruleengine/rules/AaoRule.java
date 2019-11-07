package com.alarmcontrol.server.aao.ruleengine.rules;

import com.alarmcontrol.server.aao.ruleengine.AlertContext;
import com.alarmcontrol.server.aao.ruleengine.MatchResult;

public interface AaoRule {
    MatchResult match(AlertContext alertContext);
}
