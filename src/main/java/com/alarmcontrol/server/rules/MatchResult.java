package com.alarmcontrol.server.rules;

import java.util.ArrayList;

public class MatchResult {
    private ArrayList<String> matchResults;

    public MatchResult(ArrayList<String> matchResults) {
        this.matchResults = matchResults;
    }

    public MatchResult() {
        this(new ArrayList<>());
    }

    public ArrayList<String> getResults() {
        return matchResults;
    }

    public void addDistinct(MatchResult matchResult) {
        for (var match: matchResult.getResults()) {
            if (!this.matchResults.contains(match)) {
                this.matchResults.add(match);
            }
        }
    }

    public boolean hasMatches() {
        return !matchResults.isEmpty();
    }
}
