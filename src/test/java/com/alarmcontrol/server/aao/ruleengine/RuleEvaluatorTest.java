package com.alarmcontrol.server.aao.ruleengine;

import static com.alarmcontrol.server.aao.ruleengine.Constants.ELW;
import static com.alarmcontrol.server.aao.ruleengine.Constants.F1;
import static com.alarmcontrol.server.aao.ruleengine.Constants.F2;
import static com.alarmcontrol.server.aao.ruleengine.Constants.GOTHAMCITY;
import static com.alarmcontrol.server.aao.ruleengine.Constants.H1;
import static com.alarmcontrol.server.aao.ruleengine.Constants.H1Y;
import static com.alarmcontrol.server.aao.ruleengine.Constants.HLF;
import static com.alarmcontrol.server.aao.ruleengine.Constants.METROPOLIS;
import static com.alarmcontrol.server.aao.ruleengine.Constants.OTHER_LOCATION;
import static com.alarmcontrol.server.aao.ruleengine.Constants.OWN_LOCATION;
import static com.alarmcontrol.server.aao.ruleengine.Constants.RW;
import static com.alarmcontrol.server.aao.ruleengine.Constants.TLF16;
import static org.assertj.core.api.Assertions.assertThat;

import com.alarmcontrol.server.aao.config.AaoRule;
import com.alarmcontrol.server.aao.config.AaoOrganisationConfiguration;
import com.alarmcontrol.server.aao.config.Keyword;
import com.alarmcontrol.server.aao.config.Location;
import com.alarmcontrol.server.aao.config.Vehicle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

public class RuleEvaluatorTest {

  @Test
  void alert_OneMatchingRule_ReturnsTrue() {
    AaoOrganisationConfiguration aaoConfig = test2AaoTextConfig();

    var aaoRuleA = new FluentAaoRuleBuilder()
        .createRule()
        .withLocation(GOTHAMCITY)
        .withKeyword(H1)
        .withVehicle(HLF)
        .withVehicle(ELW)
        .build();

    var aaoRules = Arrays.asList(aaoRuleA);
    var alertContext = createAlertContext(H1, GOTHAMCITY);
    var referenceContext = createReferenceContext(GOTHAMCITY);

    MatchResult matches = matchRules(aaoConfig, aaoRules, referenceContext, alertContext);

    assertThat(matches.hasMatches()).isEqualTo(true);
    assertThatVehiclesMatchExactly(matches, HLF, ELW);
  }

  @Test
  void alert_TwoRulesAndOnlyOwnCityMatch_ReturnsTrue() {
    AaoOrganisationConfiguration aaoConfig = test2AaoTextConfig();

    var aaoRuleA = new FluentAaoRuleBuilder()
        .createRule()
        .withLocation(OWN_LOCATION)
        .withKeyword(H1)
        .withVehicle(HLF)
        .withVehicle(RW)
        .build();

    var aaoRuleB = new FluentAaoRuleBuilder()
        .createRule()
        .withKeyword(H1)
        .withVehicle(RW)
        .build();

    var aaoRules = Arrays.asList(aaoRuleA, aaoRuleB);
    var alertContext = createAlertContext(H1, GOTHAMCITY);
    var referenceContext = createReferenceContext(GOTHAMCITY);

    MatchResult matches = matchRules(aaoConfig, aaoRules, referenceContext, alertContext);

    assertThat(matches.hasMatches()).isEqualTo(true);
    assertThatVehiclesMatchExactly(matches, HLF, RW);
  }

  @Test
  void alert_TwoRulesAndOnlyOtherCityMatch_ReturnsTrue() {
    AaoOrganisationConfiguration aaoConfig = test2AaoTextConfig();

    var aaoRuleA = new FluentAaoRuleBuilder()
        .createRule()
        .withLocation(OWN_LOCATION)
        .withKeyword(H1)
        .withVehicle(HLF)
        .withVehicle(RW)
        .build();

    var aaoRuleB = new FluentAaoRuleBuilder()
        .createRule()
        .withKeyword(H1)
        .withVehicle(RW)
        .build();

    var aaoRules = Arrays.asList(aaoRuleA, aaoRuleB);
    var alertContext = createAlertContext(H1, METROPOLIS);
    var referenceContext = createReferenceContext(GOTHAMCITY);

    MatchResult matches = matchRules(aaoConfig, aaoRules, referenceContext, alertContext);

    assertThat(matches.hasMatches()).isEqualTo(true);
    assertThatVehiclesMatchExactly(matches, RW);
  }


  @Test
  void alertForGothamCity_KeywordAndLocationMatches_ReturnsTrue() {
    var aaoConfig = test1AaoTextConfig();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocation(GOTHAMCITY)
        .withKeyword(H1)
        .withVehicle(ELW)
        .withVehicle(HLF)
        .build();

    var aaoRules = Arrays.asList(aaoRule);
    var alertContext = createAlertContext(H1, GOTHAMCITY);
    var referenceContext = createReferenceContext(GOTHAMCITY);

    MatchResult matches = matchRules(aaoConfig, aaoRules, referenceContext, alertContext);

    assertThat(matches.hasMatches()).isEqualTo(true);
    assertThatVehiclesMatchExactly(matches, ELW, HLF);
  }

  @Test
  void alertForH1Y_aaoKeywordDiffers_ReturnsFalse() {
    var aaoConfig = test1AaoTextConfig();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocation(GOTHAMCITY)
        .withKeyword(H1)
        .withVehicle(ELW)
        .withVehicle(HLF)
        .build();

    var aaoRules = Arrays.asList(aaoRule);
    var alertContext = createAlertContext(H1Y, GOTHAMCITY);
    var referenceContext = createReferenceContext(GOTHAMCITY);

    MatchResult matches = matchRules(aaoConfig, aaoRules, referenceContext, alertContext);

    assertThat(matches.hasMatches()).isEqualTo(false);
  }

  @Test
  void alertForMetropolis_AaoLocationDiffers_ReturnsFalse() {
    var aaoConfig = test1AaoTextConfig();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocation(GOTHAMCITY)
        .withKeyword(H1)
        .withVehicle(ELW)
        .withVehicle(HLF)
        .build();

    var aaoRules = Arrays.asList(aaoRule);
    var alertContext = createAlertContext(H1, METROPOLIS);
    var referenceContext = createReferenceContext(GOTHAMCITY);

    MatchResult matches = matchRules(aaoConfig, aaoRules, referenceContext, alertContext);

    assertThat(matches.hasMatches()).isEqualTo(false);
  }

  @Test
  void alertForHomeLocation_AaoContainsHomeLocation_ReturnsTrue() {
    var aaoConfig = test1AaoTextConfig();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocation(OWN_LOCATION)
        .withKeyword(H1)
        .withVehicle(ELW)
        .withVehicle(HLF)
        .build();

    var aaoRules = Arrays.asList(aaoRule);
    var alertContext = createAlertContext(H1, GOTHAMCITY);
    var referenceContext = createReferenceContext(GOTHAMCITY);

    MatchResult matches = matchRules(aaoConfig, aaoRules, referenceContext, alertContext);

    assertThat(matches.hasMatches()).isEqualTo(true);
    assertThatVehiclesMatchExactly(matches, ELW, HLF);
  }

  @Test
  void alertForAnyLocation_AaoContainsOtherLocations_ReturnsTrue() {
    var aaoConfig = test1AaoTextConfig();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocation(OTHER_LOCATION)
        .withKeyword(H1)
        .withVehicle(ELW)
        .withVehicle(HLF)
        .build();

    var aaoRules = Arrays.asList(aaoRule);
    var alertContext = createAlertContext(H1, METROPOLIS);
    var referenceContext = createReferenceContext(GOTHAMCITY);

    MatchResult matches = matchRules(aaoConfig, aaoRules, referenceContext, alertContext);

    assertThat(matches.hasMatches()).isEqualTo(true);
    assertThatVehiclesMatchExactly(matches, ELW, HLF);
  }

  @Test
  void specialCase_AaoWithMyLocationAndOtherLocations_ReturnsTrue() {
    var aaoConfig = test1AaoTextConfig();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocation(OTHER_LOCATION)
        .withLocation(OWN_LOCATION)
        .withKeyword(H1)
        .withVehicle(ELW)
        .withVehicle(HLF)
        .build();

    var aaoRules = Arrays.asList(aaoRule);
    var alertContext = createAlertContext(H1, GOTHAMCITY);
    var referenceContext = createReferenceContext(GOTHAMCITY);

    MatchResult matches = matchRules(aaoConfig, aaoRules, referenceContext, alertContext);

    assertThat(matches.hasMatches()).isEqualTo(true);
    assertThatVehiclesMatchExactly(matches, ELW, HLF);
  }

  @Test
  void match_AaoWithMyLocationAndOtherLocationsAndNonMatchingKeyword_ReturnsFalse() {
    var aaoConfig = test1AaoTextConfig();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocation(OTHER_LOCATION)
        .withLocation(OWN_LOCATION)
        .withKeyword(H1)
        .withVehicle(ELW)
        .withVehicle(HLF)
        .build();

    var aaoRules = Arrays.asList(aaoRule);
    var alertContext = createAlertContext(H1Y, METROPOLIS);
    var referenceContext = createReferenceContext(GOTHAMCITY);

    MatchResult matches = matchRules(aaoConfig, aaoRules, referenceContext, alertContext);

    assertThat(matches.hasMatches()).isEqualTo(false);
  }

  @Test
  void match_NoAaoRuleForAlertLocation_ReturnsFalse() {
    var aaoConfig = test1AaoTextConfig();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocation(GOTHAMCITY)
        .withKeyword(H1)
        .withVehicle(ELW)
        .withVehicle(HLF)
        .build();

    var aaoRules = Arrays.asList(aaoRule);
    var alertContext = createAlertContext(H1, METROPOLIS);
    var referenceContext = createReferenceContext(GOTHAMCITY);

    MatchResult matches = matchRules(aaoConfig, aaoRules, referenceContext, alertContext);

    assertThat(matches.hasMatches()).isEqualTo(false);
  }

  @Test
  void match_NoAaoRuleForAlertKeyword_ReturnsFalse() {
    var aaoConfig = test1AaoTextConfig();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocation(GOTHAMCITY)
        .withKeyword(H1)
        .withVehicle(ELW)
        .withVehicle(HLF)
        .build();

    var aaoRules = Arrays.asList(aaoRule);
    var alertContext = createAlertContext(H1Y, GOTHAMCITY);
    var referenceContext = createReferenceContext(METROPOLIS);

    MatchResult matches = matchRules(aaoConfig, aaoRules, referenceContext, alertContext);

    assertThat(matches.hasMatches()).isEqualTo(false);
  }

  @Test
  void alert_KeywordAndLocationMatches_VehicleOrderAsExpected1() {
    var aaoConfig = test1AaoTextConfig();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocation(GOTHAMCITY)
        .withKeyword(H1)
        .withVehicle(ELW)
        .withVehicle(HLF)
        .build();

    var aaoRules = Arrays.asList(aaoRule);
    var alertContext = createAlertContext(H1, GOTHAMCITY);
    var referenceContext = createReferenceContext(GOTHAMCITY);

    MatchResult matches = matchRules(aaoConfig, aaoRules, referenceContext, alertContext);

    assertThat(matches.hasMatches()).isEqualTo(true);
    assertThatVehiclesMatchExactly(matches, ELW, HLF);
  }

  @Test
  void alert_KeywordAndLocationMatches_VehicleOrderAsExpected2() {
    var aaoConfig = test1AaoTextConfig();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocation(GOTHAMCITY)
        .withKeyword(H1)
        .withVehicle(HLF)
        .withVehicle(ELW)
        .build();

    var aaoRules = Arrays.asList(aaoRule);
    var alertContext = createAlertContext(H1, GOTHAMCITY);
    var referenceContext = createReferenceContext(GOTHAMCITY);

    MatchResult matches = matchRules(aaoConfig, aaoRules, referenceContext, alertContext);

    assertThat(matches.hasMatches()).isEqualTo(true);
    assertThatVehiclesMatchExactly(matches, HLF, ELW);
  }


  @Test
  void alert_KeywordAndGeocodedLocationEmpty_UseOrganisationLocation() {
    var aaoConfig = test1AaoTextConfig();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocation(GOTHAMCITY)
        .withKeyword(H1)
        .withVehicle(HLF)
        .withVehicle(ELW)
        .build();

    var aaoRules = Arrays.asList(aaoRule);
    var alertContext = createAlertContext(H1, null);
    var referenceContext = createReferenceContext(GOTHAMCITY);

    MatchResult matches = matchRules(aaoConfig, aaoRules, referenceContext, alertContext);

    assertThat(matches.hasMatches()).isEqualTo(true);
    assertThatVehiclesMatchExactly(matches, HLF, ELW);
  }


  @Test
  void alert_NoOrganisationLocationProvidedAndKeywordAndLocationMatches_ReturnsTrue() {
    var aaoConfig = test1AaoTextConfig();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocation(GOTHAMCITY)
        .withKeyword(H1)
        .withVehicle(HLF)
        .withVehicle(ELW)
        .build();

    var aaoRules = Arrays.asList(aaoRule);
    var alertContext = createAlertContext(H1, GOTHAMCITY);
    var referenceContext = createReferenceContext(null);

    MatchResult matches = matchRules(aaoConfig, aaoRules, referenceContext, alertContext);

    assertThat(matches.hasMatches()).isEqualTo(true);
    assertThatVehiclesMatchExactly(matches, HLF, ELW);
  }

  @Test
  void alert_NoOrganisationLocationProvidedAndLocationNotMatching_ReturnsFalse() {
    var aaoConfig = test1AaoTextConfig();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocation(GOTHAMCITY)
        .withKeyword(H1)
        .withVehicle(HLF)
        .withVehicle(ELW)
        .build();

    var aaoRules = Arrays.asList(aaoRule);
    var alertContext = createAlertContext(H1, METROPOLIS);
    var referenceContext = createReferenceContext(null);

    MatchResult matches = matchRules(aaoConfig, aaoRules, referenceContext, alertContext);

    assertThat(matches.hasMatches()).isEqualTo(false);
  }


  @Test
  void alert_NoLocationInAaoRule_ReturnsTrue() {
    var aaoConfig = test1AaoTextConfig();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withKeyword(H1)
        .withVehicle(HLF)
        .withVehicle(ELW)
        .build();

    var aaoRules = Arrays.asList(aaoRule);
    var alertContext = createAlertContext(H1, GOTHAMCITY);
    var referenceContext = createReferenceContext(GOTHAMCITY);

    MatchResult matches = matchRules(aaoConfig, aaoRules, referenceContext, alertContext);

    assertThat(matches.hasMatches()).isEqualTo(true);
    assertThatVehiclesMatchExactly(matches, HLF, ELW);
  }


  @Test
  void alert_NoKeywordInAaoRule_ReturnsTrue() {
    var aaoConfig = test1AaoTextConfig();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocation(GOTHAMCITY)
        .withVehicle(HLF)
        .withVehicle(ELW)
        .build();

    var aaoRules = Arrays.asList(aaoRule);
    var alertContext = createAlertContext(H1, GOTHAMCITY);
    var referenceContext = createReferenceContext(GOTHAMCITY);

    MatchResult matches = matchRules(aaoConfig, aaoRules, referenceContext, alertContext);

    assertThat(matches.hasMatches()).isEqualTo(true);
    assertThatVehiclesMatchExactly(matches, HLF, ELW);
  }

  @Test
  void alert_NoKeywordAndNoLocationInAaoRule_ReturnsTrue() {
    var aaoConfig = test1AaoTextConfig();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withVehicle(HLF)
        .withVehicle(ELW)
        .build();

    var aaoRules = Arrays.asList(aaoRule);
    var alertContext = createAlertContext(H1, GOTHAMCITY);
    var referenceContext = createReferenceContext(GOTHAMCITY);

    MatchResult matches = matchRules(aaoConfig, aaoRules, referenceContext, alertContext);

    assertThat(matches.hasMatches()).isEqualTo(true);
    assertThatVehiclesMatchExactly(matches, HLF, ELW);
  }

  @Test
  void alert_KeywordIgnoreWhitespacesAndCase_ReturnsTrue() {
    var aaoConfig = test1AaoTextConfig();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocation(GOTHAMCITY)
        .withKeyword(H1Y)
        .withVehicle(HLF)
        .withVehicle(ELW)
        .build();

    var aaoRules = Arrays.asList(aaoRule);
    var alertContext = new AlertContext("H1y", new Date(), GOTHAMCITY.getName());
    var referenceContext = createReferenceContext(GOTHAMCITY);

    MatchResult matches = matchRules(aaoConfig, aaoRules, referenceContext, alertContext);

    assertThat(matches.hasMatches()).isEqualTo(true);
    assertThatVehiclesMatchExactly(matches, HLF, ELW);
  }

  @Test
  void alert_LocationIgnoreCase_ReturnsTrue() {
    var aaoConfig = test1AaoTextConfig();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocation(GOTHAMCITY)
        .withKeyword(H1Y)
        .withVehicle(HLF)
        .withVehicle(ELW)
        .build();

    var aaoRules = Arrays.asList(aaoRule);
    var alertContext = new AlertContext("H1Y", new Date(), "gothamCity");
    var referenceContext = createReferenceContext(GOTHAMCITY);

    MatchResult matches = matchRules(aaoConfig, aaoRules, referenceContext, alertContext);

    assertThat(matches.hasMatches()).isEqualTo(true);
    assertThatVehiclesMatchExactly(matches, HLF, ELW);
  }


  @Test
  void alert_NoKeywordMatch_ReturnsFalse() {
    var aaoConfig = test1AaoTextConfig();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocation(GOTHAMCITY)
        .withKeyword(H1Y)
        .withVehicle(HLF)
        .withVehicle(ELW)
        .build();

    var aaoRules = Arrays.asList(aaoRule);
    var alertContext = new AlertContext("NA", new Date(), GOTHAMCITY.getName());
    var referenceContext = createReferenceContext(GOTHAMCITY);

    MatchResult matches = matchRules(aaoConfig, aaoRules, referenceContext, alertContext);

    assertThat(matches.hasMatches()).isEqualTo(false);
  }


  private MatchResult matchRules(AaoOrganisationConfiguration aaoConfig,
      List<AaoRule> aaoRules,
      ReferenceContext referenceContext,
      AlertContext alertContext) {
    aaoConfig.setAaoRules(aaoRules);
    var rulesEvaluator = new RuleEvaluator(aaoConfig);
    return rulesEvaluator.match(referenceContext, alertContext);
  }

  @NotNull
  private ReferenceContext createReferenceContext(Location organisationLocation) {
    return new ReferenceContext(new ArrayList<>(),
        organisationLocation == null ? null : organisationLocation.getName());
  }

  @NotNull
  private AlertContext createAlertContext(Keyword keyword, Location alertLocation) {
    return new AlertContext(
        keyword.getKeyword(),
        new Date(),
        alertLocation == null ? null : alertLocation.getName());
  }

  private void assertThatVehiclesMatchExactly(MatchResult matches, Vehicle... vehicles) {
    String[] vehicleNames = Arrays.asList(vehicles)
        .stream()
        .map(v -> v.getName())
        .collect(Collectors.toList())
        .toArray(new String[]{});

    assertThat(matches.getResults()).containsExactly(vehicleNames);
  }

  private AaoOrganisationConfiguration test2AaoTextConfig() {
    return new FluentAaoConfigBuilder()
        .createConfig()
        .withLocation(GOTHAMCITY)
        .withLocation(METROPOLIS)
        .withKeyword(H1)
        .withKeyword(H1Y)
        .withKeyword(F1)
        .withKeyword(F2)
        .withVehicle(ELW)
        .withVehicle(HLF)
        .withVehicle(RW)
        .withVehicle(TLF16)
        .build();
  }

  private AaoOrganisationConfiguration test1AaoTextConfig() {
    return new FluentAaoConfigBuilder()
        .createConfig()
        .withLocation(GOTHAMCITY)
        .withKeyword(H1)
        .withKeyword(H1Y)
        .withVehicle(ELW)
        .withVehicle(HLF)
        .build();
  }
}