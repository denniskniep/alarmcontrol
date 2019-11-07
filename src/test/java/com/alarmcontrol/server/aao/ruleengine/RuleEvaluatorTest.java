package com.alarmcontrol.server.aao.ruleengine;

import static org.assertj.core.api.Assertions.assertThat;

import com.alarmcontrol.server.aao.config.Aao;
import com.alarmcontrol.server.aao.config.AaoOrganisationConfiguration;
import com.alarmcontrol.server.aao.ruleengine.rules.KeywordAndLocationMatchRule;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class RuleEvaluatorTest {

  public static final String GOTHAMCITY = "Gothamcity";
  public static final String METROPOLIS = "Metropolis";

  private static final String H1_ID = "64510d39-e35f-436c-897d-3c53fec3ead8";
  private static final String H1Y_ID = "219bf9f4-8f9b-465f-9da3-17b336dd8390";
  private static final String F1_ID = "8f9b-64510d39";
  private static final String F2_ID = "65f-9da3-897d";

  @Test
  void alert_OneMatchingRule_ReturnsTrue() {
    AaoOrganisationConfiguration aaoConfig = defaultAaoTextConfig();

    var aaoRuleA = new FluentAaoRuleBuilder()
        .createRule()
        .withLocationId(GOTHAMCITY)
        .withKeywordId(H1_ID)
        .withVehicleId("HLF")
        .withVehicleId("ELW")
        .build();

    var aaoRules = new ArrayList<>(Arrays.asList(aaoRuleA));
    var alertContext = new AlertContext("H1", LocalTime.now(), 1L, GOTHAMCITY, GOTHAMCITY);

    MatchResult matches = matchRules(aaoConfig, aaoRules, alertContext);

    assertThat(matches.hasMatches()).isEqualTo(true);
    assertThat(matches.getResults()).containsExactly("HLF", "ELW");
  }
  @Test
  void alert_TwoRulesAndOnlyOwnCityMatch_ReturnsTrue() {
    AaoOrganisationConfiguration aaoConfig = defaultAaoTextConfig();

    var aaoRuleA = new FluentAaoRuleBuilder()
        .createRule()
        .withLocationId(KeywordAndLocationMatchRule.OwnOrganisationUniqueKey)
        .withKeywordId(H1_ID)
        .withVehicleId("HLF")
        .withVehicleId("RW")
        .build();

    var aaoRuleB = new FluentAaoRuleBuilder()
        .createRule()
        .withKeywordId(KeywordAndLocationMatchRule.OtherOrganisationsUniqueKey)
        .withKeywordId(H1_ID)
        .withVehicleId("RW")
        .build();

    var aaoRules = new ArrayList<>(Arrays.asList(aaoRuleA, aaoRuleB));
    var alertContext = new AlertContext("H1", LocalTime.now(), 1L, GOTHAMCITY, GOTHAMCITY);

    MatchResult matches = matchRules(aaoConfig, aaoRules, alertContext);

    assertThat(matches.hasMatches()).isEqualTo(true);
    assertThat(matches.getResults()).containsExactly("HLF", "RW");
  }

  @Test
  void alert_TwoRulesAndOnlyOtherCityMatch_ReturnsTrue() {
    AaoOrganisationConfiguration aaoConfig = defaultAaoTextConfig();

    var aaoRuleA = new FluentAaoRuleBuilder()
        .createRule()
        .withLocationId(KeywordAndLocationMatchRule.OwnOrganisationUniqueKey)
        .withKeywordId(H1_ID)
        .withVehicleId("HLF")
        .withVehicleId("RW")
        .build();

    var aaoRuleB = new FluentAaoRuleBuilder()
        .createRule()
        .withKeywordId(KeywordAndLocationMatchRule.OtherOrganisationsUniqueKey)
        .withKeywordId(H1_ID)
        .withVehicleId("RW")
        .build();

    var aaoRules = new ArrayList<>(Arrays.asList(aaoRuleA, aaoRuleB));
    var alertContext = new AlertContext("H1", LocalTime.now(), 1L, METROPOLIS, GOTHAMCITY);

    MatchResult matches = matchRules(aaoConfig, aaoRules, alertContext);

    assertThat(matches.hasMatches()).isEqualTo(true);
    assertThat(matches.getResults()).containsExactly("RW");
  }

  private MatchResult matchRules(AaoOrganisationConfiguration aaoConfig, ArrayList<Aao> aaoRules,
      AlertContext alertContext) {
    aaoConfig.setAaoRules(aaoRules);
    var rulesEvaluator = new RuleEvaluator(aaoConfig);
    return rulesEvaluator.match(alertContext);
  }

  private AaoOrganisationConfiguration defaultAaoTextConfig() {
    return new FluentAaoConfigBuilder()
          .createConfig()
          .withLocation(GOTHAMCITY, GOTHAMCITY)
          .withKeyword(H1_ID, "H 1")
          .withKeyword(H1Y_ID, "H 1 Y")
          .withKeyword(F1_ID, "F 1")
          .withKeyword(F2_ID, "F 2")
          .withVehicle("ELW", "ELW")
          .withVehicle("HLF", "HLF")
          .withVehicle("RW", "RW")
          .withVehicle("TLF16/25", "TLF16/25")
          .build();
  }
}