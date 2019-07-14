package com.alarmcontrol.server.rules;

import java.util.ArrayList;

public class RuleEvaluator {
    private ArrayList<Rule> rules;
    private ArrayList<String> results;

    public RuleEvaluator(ArrayList<Rule> rules, ArrayList<String> results) {
        this.rules = rules;
        this.results = new ArrayList<>(results);
    }

    public MatchResult match(AlertContext alertContext) {
        for (var rule : rules) {
            if (!rule.match(alertContext)) {
                return new MatchResult();
            }
        }
        return new MatchResult(this.results);
    }
}
