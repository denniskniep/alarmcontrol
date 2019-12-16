package com.alarmcontrol.server.aao.ruleengine.rules;

import com.alarmcontrol.server.aao.ruleengine.AlertContext;
import com.alarmcontrol.server.aao.ruleengine.ReferenceContext;

public interface Rule {
    boolean match(ReferenceContext referenceContext, AlertContext alertContext);
}
