package com.alarmcontrol.server.aao.ruleengine;

import com.alarmcontrol.server.aao.config.AaoOrganisationConfiguration;
import com.alarmcontrol.server.aao.ruleengine.rules.KeywordAndLocationMatchRule;

public class RuleEvaluator {

  private AaoOrganisationConfiguration aaoConfig;

  public RuleEvaluator(AaoOrganisationConfiguration aaoConfig) {
    this.aaoConfig = aaoConfig;
  }

    public MatchResult match(AlertContext alertContext) {
      var aaoRules = aaoConfig.getAaoRules();
      for(var aaoRule : aaoRules) {
        var keywordAndLocationRule = new KeywordAndLocationMatchRule(aaoRule, aaoConfig);
        var matchResult = keywordAndLocationRule.match(alertContext);
        if(matchResult.hasMatches()){
          return matchResult;
        }
      }

      return new MatchResult();
    }
}
/*

Spezialfahrzeuge sind wichtiger und werden immer dazuaddiert!
 Sub 0815 -> DLK
 Sub 0815 -> RW

 DLK, RW
 ---
 Sub01, Sub0815
 ------------
 Sub 01
 Alarm für Calden
 Vormittags, Meimbressen xy -> RW, TLF
 Abends, Meimbressen xy -> RW






 */