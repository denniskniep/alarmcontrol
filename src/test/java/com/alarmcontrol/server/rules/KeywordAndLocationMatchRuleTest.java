package com.alarmcontrol.server.rules;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

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
                .withLocation(KeywordAndLocationMatchRule.OtherOrganisationsUniqueKey, "Other")
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
    }

    @Test
    void alertForH1Y_aaoKeywordDiffers_ReturnsFalse() {
        var aaoConfig = new FluentAaoConfigBuilder()
                .createConfig()
                .withLocation(GOTHAMCITY, GOTHAMCITY)
                .withLocation(KeywordAndLocationMatchRule.OtherOrganisationsUniqueKey, "Other")
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
                .withLocation(KeywordAndLocationMatchRule.OtherOrganisationsUniqueKey, "Other")
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
                .withLocation(KeywordAndLocationMatchRule.OtherOrganisationsUniqueKey, "Other")
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
    }

    @Test
    void alertForAnyLocation_AaoContainsOtherLocations_ReturnsTrue() {
        var aaoConfig = new FluentAaoConfigBuilder()
                .createConfig()
                .withLocation(GOTHAMCITY, GOTHAMCITY)
                .withLocation(KeywordAndLocationMatchRule.OtherOrganisationsUniqueKey, "Other")
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
    }

    @Test
    void specialCase_AaoWithMyLocationAndOtherLocations_ReturnsTrue(){
        var aaoConfig = new FluentAaoConfigBuilder()
                .createConfig()
                .withLocation(GOTHAMCITY, GOTHAMCITY)
                .withLocation(KeywordAndLocationMatchRule.OtherOrganisationsUniqueKey, "Other")
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
    }

    @Test
    void match_AaoWithMyLocationAndOtherLocationsAndNonMatchingKeyword_ReturnsFalse() {
        var aaoConfig = new FluentAaoConfigBuilder()
                .createConfig()
                .withLocation(GOTHAMCITY, GOTHAMCITY)
                .withLocation(KeywordAndLocationMatchRule.OtherOrganisationsUniqueKey, "Other")
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
                .withLocation(KeywordAndLocationMatchRule.OtherOrganisationsUniqueKey, "Other")
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
                .withLocation(KeywordAndLocationMatchRule.OtherOrganisationsUniqueKey, "Other")
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
                .withLocation(KeywordAndLocationMatchRule.OtherOrganisationsUniqueKey, "Other")
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

        assertThat(matches.getResults().get(0)).isEqualTo("ELW");
        assertThat(matches.getResults().get(1)).isEqualTo("HLF");
    }

    @Test
    void alert_KeywordAndLocationMatches_VehicleOrderAsExpected2() {
        var aaoConfig = new FluentAaoConfigBuilder()
                .createConfig()
                .withLocation(GOTHAMCITY, GOTHAMCITY)
                .withLocation(KeywordAndLocationMatchRule.OtherOrganisationsUniqueKey, "Other")
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

        assertThat(matches.getResults().get(0)).isEqualTo("HLF");
        assertThat(matches.getResults().get(1)).isEqualTo("ELW");
    }
}

