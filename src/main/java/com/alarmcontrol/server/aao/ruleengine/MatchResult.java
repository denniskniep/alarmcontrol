package com.alarmcontrol.server.aao.ruleengine;

import java.util.ArrayList;
import java.util.List;

public class MatchResult {
    private List<String> matchResults;
    private String matchedRuleUid;

    public MatchResult(String matchedRuleUid, List<String> matchResults) {
        this.matchedRuleUid = matchedRuleUid;
        this.matchResults = matchResults;
    }

    public MatchResult() {
        this(null, new ArrayList<>());
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
        return matchedRuleUid != null;
    }
}
