package com.alarmcontrol.server.aao.ruleengine.rules;

import com.alarmcontrol.server.aao.ruleengine.AlertContext;
import com.alarmcontrol.server.aao.ruleengine.FluentAaoConfigBuilder;
import com.alarmcontrol.server.aao.ruleengine.FluentAaoRuleBuilder;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;

public class KeywordAndLocationMatchRuleTest {

  public static final String GOTHAMCITY = "Gothamcity";
  public static final String METROPOLIS = "Metropolis";

  private static final String H1UNIQUEID = "64510d39-e35f-436c-897d-3c53fec3ead8";
  private static final String H1YUNIQUEID = "219bf9f4-8f9b-465f-9da3-17b336dd8390";

  @Test
  void alertForGothamCity_KeywordAndLocationMatches_ReturnsTrue() {
    var aaoConfig = new FluentAaoConfigBuilder()
        .createConfig()
        .withLocation(GOTHAMCITY, GOTHAMCITY)
        .withKeyword(H1UNIQUEID, "H 1")
        .withKeyword(H1YUNIQUEID, "H 1 Y")
        .withVehicle("ELW", "ELW")
        .withVehicle("HLF", "HLF")
        .build();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocationId(GOTHAMCITY)
        .withKeywordId(H1UNIQUEID)
        .withVehicleId("ELW")
        .withVehicleId("HLF")
        .build();

    var rule = new KeywordAndLocationMatchRule(aaoRule, aaoConfig);
    var alertContext = new AlertContext("H 1", LocalTime.now(), 1L, GOTHAMCITY, GOTHAMCITY);
    var matches = rule.match(alertContext);

    assertThat(matches.hasMatches()).isEqualTo(true);
    assertThat(matches.getResults()).containsExactly("ELW", "HLF");
  }

  @Test
  void alertForH1Y_aaoKeywordDiffers_ReturnsFalse() {
    var aaoConfig = new FluentAaoConfigBuilder()
        .createConfig()
        .withLocation(GOTHAMCITY, GOTHAMCITY)
        .withKeyword(H1UNIQUEID, "H 1")
        .withKeyword(H1YUNIQUEID, "H 1 Y")
        .withVehicle("ELW", "ELW")
        .withVehicle("HLF", "HLF")
        .build();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocationId(GOTHAMCITY)
        .withKeywordId(H1UNIQUEID)
        .withVehicleId("ELW")
        .withVehicleId("HLF")
        .build();

    var rule = new KeywordAndLocationMatchRule(aaoRule, aaoConfig);
    var alertContext = new AlertContext("H 1 Y", LocalTime.now(), 1L, GOTHAMCITY, GOTHAMCITY);
    var matches = rule.match(alertContext);

    assertThat(matches.hasMatches()).isEqualTo(false);
  }

  @Test
  void alertForMetropolis_AaoLocationDiffers_ReturnsFalse() {
    var aaoConfig = new FluentAaoConfigBuilder()
        .createConfig()
        .withLocation(GOTHAMCITY, GOTHAMCITY)
        .withKeyword(H1UNIQUEID, "H 1")
        .withKeyword(H1YUNIQUEID, "H 1 Y")
        .withVehicle("ELW", "ELW")
        .withVehicle("HLF", "HLF")
        .build();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocationId(GOTHAMCITY)
        .withKeywordId(H1UNIQUEID)
        .withVehicleId("ELW")
        .withVehicleId("HLF")
        .build();

    var rule = new KeywordAndLocationMatchRule(aaoRule, aaoConfig);
    var alertContext = new AlertContext("H 1", LocalTime.now(), 1L, METROPOLIS, GOTHAMCITY);
    var matches = rule.match(alertContext);

    assertThat(matches.hasMatches()).isEqualTo(false);
  }

  @Test
  void alertForHomeLocation_AaoContainsHomeLocation_ReturnsTrue() {
    var aaoConfig = new FluentAaoConfigBuilder()
        .createConfig()
        .withLocation(GOTHAMCITY, GOTHAMCITY)
        .withKeyword(H1UNIQUEID, "H 1")
        .withKeyword(H1YUNIQUEID, "H 1 Y")
        .withVehicle("ELW", "ELW")
        .withVehicle("HLF", "HLF")
        .build();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocationId(KeywordAndLocationMatchRule.OwnOrganisationUniqueKey)
        .withKeywordId(H1UNIQUEID)
        .withVehicleId("ELW")
        .withVehicleId("HLF")
        .build();

    var rule = new KeywordAndLocationMatchRule(aaoRule, aaoConfig);
    var alertContext = new AlertContext("H 1", LocalTime.now(), 1L, "MyHomeTown", "MyHomeTown");
    var matches = rule.match(alertContext);

    assertThat(matches.hasMatches()).isEqualTo(true);
    assertThat(matches.getResults()).containsExactly("ELW", "HLF");
  }

  @Test
  void alertForAnyLocation_AaoContainsOtherLocations_ReturnsTrue() {
    var aaoConfig = new FluentAaoConfigBuilder()
        .createConfig()
        .withLocation(GOTHAMCITY, GOTHAMCITY)
        .withKeyword(H1UNIQUEID, "H 1")
        .withKeyword(H1YUNIQUEID, "H 1 Y")
        .withVehicle("ELW", "ELW")
        .withVehicle("HLF", "HLF")
        .build();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocationId(KeywordAndLocationMatchRule.OtherOrganisationsUniqueKey)
        .withKeywordId(H1UNIQUEID)
        .withVehicleId("ELW")
        .withVehicleId("HLF")
        .build();

    var rule = new KeywordAndLocationMatchRule(aaoRule, aaoConfig);
    var alertContext = new AlertContext("H 1", LocalTime.now(), 1L, METROPOLIS, GOTHAMCITY);
    var matches = rule.match(alertContext);

    assertThat(matches.hasMatches()).isEqualTo(true);
    assertThat(matches.getResults()).containsExactly("ELW", "HLF");
  }

  @Test
  void specialCase_AaoWithMyLocationAndOtherLocations_ReturnsTrue() {
    var aaoConfig = new FluentAaoConfigBuilder()
        .createConfig()
        .withLocation(GOTHAMCITY, GOTHAMCITY)
        .withKeyword(H1UNIQUEID, "H 1")
        .withKeyword(H1YUNIQUEID, "H 1 Y")
        .withVehicle("ELW", "ELW")
        .withVehicle("HLF", "HLF")
        .build();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocationId(KeywordAndLocationMatchRule.OtherOrganisationsUniqueKey)
        .withLocationId(KeywordAndLocationMatchRule.OwnOrganisationUniqueKey)
        .withKeywordId(H1UNIQUEID)
        .withVehicleId("ELW")
        .withVehicleId("HLF")
        .build();

    var rule = new KeywordAndLocationMatchRule(aaoRule, aaoConfig);
    var alertContext = new AlertContext("H 1", LocalTime.now(), 1L, GOTHAMCITY, GOTHAMCITY);
    var matches = rule.match(alertContext);

    assertThat(matches.hasMatches()).isEqualTo(true);
    assertThat(matches.getResults()).containsExactly("ELW", "HLF");
  }

  @Test
  void match_AaoWithMyLocationAndOtherLocationsAndNonMatchingKeyword_ReturnsFalse() {
    var aaoConfig = new FluentAaoConfigBuilder()
        .createConfig()
        .withLocation(GOTHAMCITY, GOTHAMCITY)
        .withKeyword(H1UNIQUEID, "H 1")
        .withKeyword(H1YUNIQUEID, "H 1 Y")
        .withVehicle("ELW", "ELW")
        .withVehicle("HLF", "HLF")
        .build();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocationId(KeywordAndLocationMatchRule.OtherOrganisationsUniqueKey)
        .withLocationId(KeywordAndLocationMatchRule.OwnOrganisationUniqueKey)
        .withKeywordId(H1UNIQUEID)
        .withVehicleId("ELW")
        .withVehicleId("HLF")
        .build();

    var rule = new KeywordAndLocationMatchRule(aaoRule, aaoConfig);
    var alertContext = new AlertContext("H 1 Y", LocalTime.now(), 1L, METROPOLIS, GOTHAMCITY);
    var matches = rule.match(alertContext);

    assertThat(matches.hasMatches()).isEqualTo(false);
  }

  @Test
  void match_NoAaoRuleForAlertLocation_ReturnsFalse() {
    var aaoConfig = new FluentAaoConfigBuilder()
        .createConfig()
        .withLocation(GOTHAMCITY, GOTHAMCITY)
        .withKeyword(H1UNIQUEID, "H 1")
        .withKeyword(H1YUNIQUEID, "H 1 Y")
        .withVehicle("ELW", "ELW")
        .withVehicle("HLF", "HLF")
        .build();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocationId(GOTHAMCITY)
        .withKeywordId(H1UNIQUEID)
        .withVehicleId("ELW")
        .withVehicleId("HLF")
        .build();

    var rule = new KeywordAndLocationMatchRule(aaoRule, aaoConfig);
    var alertContext = new AlertContext("H 1", LocalTime.now(), 1L, METROPOLIS, GOTHAMCITY);
    var matches = rule.match(alertContext);

    assertThat(matches.hasMatches()).isEqualTo(false);
  }

  @Test
  void match_NoAaoRuleForAlertKeyword_ReturnsFalse() {
    var aaoConfig = new FluentAaoConfigBuilder()
        .createConfig()
        .withLocation(GOTHAMCITY, GOTHAMCITY)
        .withKeyword(H1UNIQUEID, "H 1")
        .withKeyword(H1YUNIQUEID, "H 1 Y")
        .withVehicle("ELW", "ELW")
        .withVehicle("HLF", "HLF")
        .build();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocationId(GOTHAMCITY)
        .withKeywordId(H1UNIQUEID)
        .withVehicleId("ELW")
        .withVehicleId("HLF")
        .build();

    var rule = new KeywordAndLocationMatchRule(aaoRule, aaoConfig);
    var alertContext = new AlertContext("H 1 Y", LocalTime.now(), 1L, GOTHAMCITY, METROPOLIS);
    var matches = rule.match(alertContext);

    assertThat(matches.hasMatches()).isEqualTo(false);
  }

  @Test
  void alert_KeywordAndLocationMatches_VehicleOrderAsExpected1() {
    var aaoConfig = new FluentAaoConfigBuilder()
        .createConfig()
        .withLocation(GOTHAMCITY, GOTHAMCITY)
        .withKeyword(H1UNIQUEID, "H 1")
        .withKeyword(H1YUNIQUEID, "H 1 Y")
        .withVehicle("ELW", "ELW")
        .withVehicle("HLF", "HLF")
        .build();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocationId(GOTHAMCITY)
        .withKeywordId(H1UNIQUEID)
        .withVehicleId("ELW")
        .withVehicleId("HLF")
        .build();

    var rule = new KeywordAndLocationMatchRule(aaoRule, aaoConfig);
    var alertContext = new AlertContext("H 1", LocalTime.now(), 1L, GOTHAMCITY, GOTHAMCITY);
    var matches = rule.match(alertContext);

    assertThat(matches.getResults()).containsExactly("ELW", "HLF");;
  }

  @Test
  void alert_KeywordAndLocationMatches_VehicleOrderAsExpected2() {
    var aaoConfig = new FluentAaoConfigBuilder()
        .createConfig()
        .withLocation(GOTHAMCITY, GOTHAMCITY)
        .withKeyword(H1UNIQUEID, "H 1")
        .withKeyword(H1YUNIQUEID, "H 1 Y")
        .withVehicle("ELW", "ELW")
        .withVehicle("HLF", "HLF")
        .build();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocationId(GOTHAMCITY)
        .withKeywordId(H1UNIQUEID)
        .withVehicleId("HLF")
        .withVehicleId("ELW")
        .build();

    var rule = new KeywordAndLocationMatchRule(aaoRule, aaoConfig);
    var alertContext = new AlertContext("H 1", LocalTime.now(), 1L, GOTHAMCITY, GOTHAMCITY);
    var matches = rule.match(alertContext);

    assertThat(matches.getResults()).containsExactly("HLF", "ELW");
  }

  @Test
  void alert_KeywordAndGeocodedLocationEmpty_UseOrganisationLocation() {
    var aaoConfig = new FluentAaoConfigBuilder()
        .createConfig()
        .withLocation(GOTHAMCITY, GOTHAMCITY)
        .withKeyword(H1UNIQUEID, "H 1")
        .withKeyword(H1YUNIQUEID, "H 1 Y")
        .withVehicle("ELW", "ELW")
        .withVehicle("HLF", "HLF")
        .build();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocationId(GOTHAMCITY)
        .withKeywordId(H1UNIQUEID)
        .withVehicleId("HLF")
        .withVehicleId("ELW")
        .build();

    var rule = new KeywordAndLocationMatchRule(aaoRule, aaoConfig);
    var alertContext = new AlertContext("H 1", LocalTime.now(), 1L, null, GOTHAMCITY);
    var matches = rule.match(alertContext);

    assertThat(matches.getResults()).containsExactly("HLF", "ELW");
  }

  @Test
  void alert_NoOrganisationLocationProvidedAndKeywordAndLocationMatches_ReturnsTrue() {
    var aaoConfig = new FluentAaoConfigBuilder()
        .createConfig()
        .withLocation(GOTHAMCITY, GOTHAMCITY)
        .withKeyword(H1UNIQUEID, "H 1")
        .withKeyword(H1YUNIQUEID, "H 1 Y")
        .withVehicle("ELW", "ELW")
        .withVehicle("HLF", "HLF")
        .build();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocationId(GOTHAMCITY)
        .withKeywordId(H1UNIQUEID)
        .withVehicleId("HLF")
        .withVehicleId("ELW")
        .build();

    var rule = new KeywordAndLocationMatchRule(aaoRule, aaoConfig);
    var alertContext = new AlertContext("H 1", LocalTime.now(), 1L, GOTHAMCITY, null);
    var matches = rule.match(alertContext);

    assertThat(matches.getResults()).containsExactly("HLF", "ELW");
  }

  @Test
  void alert_NoOrganisationLocationProvidedUseOrganisationLocation_ReturnsFalse() {
    var aaoConfig = new FluentAaoConfigBuilder()
        .createConfig()
        .withLocation(GOTHAMCITY, GOTHAMCITY)
        .withKeyword(H1UNIQUEID, "H 1")
        .withKeyword(H1YUNIQUEID, "H 1 Y")
        .withVehicle("ELW", "ELW")
        .withVehicle("HLF", "HLF")
        .build();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocationId(GOTHAMCITY)
        .withKeywordId(H1UNIQUEID)
        .withVehicleId("HLF")
        .withVehicleId("ELW")
        .build();

    var rule = new KeywordAndLocationMatchRule(aaoRule, aaoConfig);
    var alertContext = new AlertContext("H 1", LocalTime.now(), 1L, KeywordAndLocationMatchRule.OwnOrganisationUniqueKey, null);
    var matches = rule.match(alertContext);

    assertThat(matches.hasMatches()).isEqualTo(false);
  }

  @Test
  void alert_NoOrganisationLocationProvidedUseOtherLocation_ReturnsFalse() {
    var aaoConfig = new FluentAaoConfigBuilder()
        .createConfig()
        .withLocation(GOTHAMCITY, GOTHAMCITY)
        .withKeyword(H1UNIQUEID, "H 1")
        .withKeyword(H1YUNIQUEID, "H 1 Y")
        .withVehicle("ELW", "ELW")
        .withVehicle("HLF", "HLF")
        .build();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocationId(GOTHAMCITY)
        .withKeywordId(H1UNIQUEID)
        .withVehicleId("HLF")
        .withVehicleId("ELW")
        .build();

    var rule = new KeywordAndLocationMatchRule(aaoRule, aaoConfig);
    var alertContext = new AlertContext("H 1", LocalTime.now(), 1L, KeywordAndLocationMatchRule.OtherOrganisationsUniqueKey, null);
    var matches = rule.match(alertContext);

    assertThat(matches.hasMatches()).isEqualTo(false);
  }

  @Test
  void alert_NoLocationInAaoRule_ReturnsTrue() {
    var aaoConfig = new FluentAaoConfigBuilder()
        .createConfig()
        .withLocation(GOTHAMCITY, GOTHAMCITY)
        .withKeyword(H1UNIQUEID, "H 1")
        .withKeyword(H1YUNIQUEID, "H 1 Y")
        .withVehicle("ELW", "ELW")
        .withVehicle("HLF", "HLF")
        .build();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withKeywordId(H1UNIQUEID)
        .withVehicleId("HLF")
        .withVehicleId("ELW")
        .build();

    var rule = new KeywordAndLocationMatchRule(aaoRule, aaoConfig);
    var alertContext = new AlertContext("H 1", LocalTime.now(), 1L, GOTHAMCITY, GOTHAMCITY);
    var matches = rule.match(alertContext);

    assertThat(matches.hasMatches()).isEqualTo(true);
    assertThat(matches.getResults()).containsExactly("HLF", "ELW");
  }

  @Test
  void alert_NoKeywordInAaoRule_ReturnsTrue() {
    var aaoConfig = new FluentAaoConfigBuilder()
        .createConfig()
        .withLocation(GOTHAMCITY, GOTHAMCITY)
        .withKeyword(H1UNIQUEID, "H 1")
        .withKeyword(H1YUNIQUEID, "H 1 Y")
        .withVehicle("ELW", "ELW")
        .withVehicle("HLF", "HLF")
        .build();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocationId(GOTHAMCITY)
        .withVehicleId("HLF")
        .withVehicleId("ELW")
        .build();

    var rule = new KeywordAndLocationMatchRule(aaoRule, aaoConfig);
    var alertContext = new AlertContext("H 1", LocalTime.now(), 1L, GOTHAMCITY, GOTHAMCITY);
    var matches = rule.match(alertContext);

    assertThat(matches.hasMatches()).isEqualTo(true);
    assertThat(matches.getResults()).containsExactly("HLF", "ELW");
  }

  @Test
  void alert_NoKeywordAndNoLocationInAaoRule_ReturnsTrue() {
    var aaoConfig = new FluentAaoConfigBuilder()
        .createConfig()
        .withLocation(GOTHAMCITY, GOTHAMCITY)
        .withKeyword(H1UNIQUEID, "H 1")
        .withKeyword(H1YUNIQUEID, "H 1 Y")
        .withVehicle("ELW", "ELW")
        .withVehicle("HLF", "HLF")
        .build();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withVehicleId("HLF")
        .withVehicleId("ELW")
        .build();

    var rule = new KeywordAndLocationMatchRule(aaoRule, aaoConfig);
    var alertContext = new AlertContext("H 1", LocalTime.now(), 1L, GOTHAMCITY, GOTHAMCITY);
    var matches = rule.match(alertContext);

    assertThat(matches.hasMatches()).isEqualTo(true);
    assertThat(matches.getResults()).containsExactly("HLF", "ELW");
  }

  @Test
  void alert_KeywordIgnoreWhitespacesAndCase_ReturnsTrue() {
    var aaoConfig = new FluentAaoConfigBuilder()
        .createConfig()
        .withLocation(GOTHAMCITY, GOTHAMCITY)
        .withKeyword(H1UNIQUEID, "H 1")
        .withKeyword(H1YUNIQUEID, "H 1 Y")
        .withVehicle("ELW", "ELW")
        .withVehicle("HLF", "HLF")
        .build();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocationId(GOTHAMCITY)
        .withKeywordId(H1YUNIQUEID)
        .withVehicleId("HLF")
        .withVehicleId("ELW")
        .build();

    var rule = new KeywordAndLocationMatchRule(aaoRule, aaoConfig);
    var alertContext = new AlertContext("H1y", LocalTime.now(), 1L, GOTHAMCITY, GOTHAMCITY);
    var matches = rule.match(alertContext);

    assertThat(matches.hasMatches()).isEqualTo(true);
    assertThat(matches.getResults()).containsExactly("HLF", "ELW");
  }

  @Test
  void alert_LocationIgnoreCase_ReturnsTrue() {
    var aaoConfig = new FluentAaoConfigBuilder()
        .createConfig()
        .withLocation(GOTHAMCITY, GOTHAMCITY)
        .withKeyword(H1UNIQUEID, "H 1")
        .withKeyword(H1YUNIQUEID, "H 1 Y")
        .withVehicle("ELW", "ELW")
        .withVehicle("HLF", "HLF")
        .build();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocationId(GOTHAMCITY)
        .withKeywordId(H1YUNIQUEID)
        .withVehicleId("HLF")
        .withVehicleId("ELW")
        .build();

    var rule = new KeywordAndLocationMatchRule(aaoRule, aaoConfig);
    var alertContext = new AlertContext("H1Y", LocalTime.now(), 1L, "gothamCity", GOTHAMCITY);
    var matches = rule.match(alertContext);

    assertThat(matches.hasMatches()).isEqualTo(true);
    assertThat(matches.getResults()).containsExactly("HLF", "ELW");
  }

  @Test
  void alert_NoKeywordMatch_ReturnsFalse() {
    var aaoConfig = new FluentAaoConfigBuilder()
        .createConfig()
        .withLocation(GOTHAMCITY, GOTHAMCITY)
        .withKeyword(H1UNIQUEID, "H 1")
        .withKeyword(H1YUNIQUEID, "H 1 Y")
        .withVehicle("ELW", "ELW")
        .withVehicle("HLF", "HLF")
        .build();

    var aaoRule = new FluentAaoRuleBuilder()
        .createRule()
        .withLocationId(GOTHAMCITY)
        .withKeywordId(H1YUNIQUEID)
        .withVehicleId("HLF")
        .withVehicleId("ELW")
        .build();

    var rule = new KeywordAndLocationMatchRule(aaoRule, aaoConfig);
    var alertContext = new AlertContext("NA", LocalTime.now(), 1L, GOTHAMCITY, GOTHAMCITY);
    var matches = rule.match(alertContext);

    assertThat(matches.hasMatches()).isEqualTo(false);
  }
}

