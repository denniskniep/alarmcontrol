package com.alarmcontrol.server.rules.data;

import java.util.ArrayList;

public class RuleContainerDataCollection {
    private ArrayList<RuleContainerData> ruleContainers;

    public RuleContainerDataCollection(ArrayList<RuleContainerData> ruleContainers) {
        this.ruleContainers = ruleContainers;
    }

    public RuleContainerDataCollection() {
        this(new ArrayList<>());
    }

    public void setRuleContainers(ArrayList<RuleContainerData> ruleContainers) {
        this.ruleContainers = ruleContainers;
    }

    public ArrayList<RuleContainerData> getRuleContainers() {
        return ruleContainers;
    }
}

