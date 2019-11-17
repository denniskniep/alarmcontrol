package com.alarmcontrol.server.aao.ruleengine;

import java.util.ArrayList;
import java.util.List;

public class MatchResult {
    private List<String> matchResults;

    public MatchResult(List<String> matchResults) {
        this.matchResults = matchResults;
    }

    public MatchResult() {
        this(new ArrayList<>());
    }

    public List<String> getResults() {
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
