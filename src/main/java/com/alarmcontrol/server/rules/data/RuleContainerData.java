package com.alarmcontrol.server.rules.data;

import java.util.ArrayList;

public class RuleContainerData {
    private ArrayList<RuleData> rules;
    private ArrayList<String> results;

    public ArrayList<String> getResults() {
        return results;
    }

    public void setResults(ArrayList<String> results) {
        this.results = results;
    }

    public ArrayList<RuleData> getRules() {
        return rules;
    }

    public void setRules(ArrayList<RuleData> rules) {

        this.rules = rules;
    }
}
