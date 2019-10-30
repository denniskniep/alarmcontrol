package com.alarmcontrol.server.rules;

import com.alarmcontrol.server.aaos.Aao;
import com.alarmcontrol.server.aaos.CatalogKeywordInput;
import com.alarmcontrol.server.aaos.Location;
import com.alarmcontrol.server.aaos.Vehicle;
import com.alarmcontrol.server.notifications.core.config.AaoOrganisationConfiguration;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class KeywordAndLocationMatchRuleTest {

    public static final String GOTHAMCITY = "Gothamcity";
    public static final String METROPOLIS = "Metropolis";

    @Test
    void match_KeywordAndLocationMatching_ReturnsTrue() {
        MatchResult matchResult = runTest("H 1", GOTHAMCITY, "Anywhere");

        assertThat(matchResult.hasMatches()).isEqualTo(true);
    }

    @Test
    void match_KeywordDiffers_ReturnsFalse() {
        MatchResult matchResult = runTest("H 1 Y", GOTHAMCITY,"Anywhere");

        assertThat(matchResult.hasMatches()).isEqualTo(false);
    }

    @Test
    void match_LocationDiffers_ReturnsFalse() {
        MatchResult matchResult = runTest("H 1", METROPOLIS,"Anywhere");

        assertThat(matchResult.hasMatches()).isEqualTo(false);
    }

    @Test
    void match_AaoContainsHomeLocation_ReturnsTrue() {
        MatchResult matchResult = runTest("H 1", METROPOLIS, METROPOLIS);

        assertThat(matchResult.hasMatches()).isEqualTo(true);
    }

   /* @Test
    void match_AaoSaysAllLocationsExceptOfHomeLocation_ReturnsTrue() {
        MatchResult matchResult = runTest("H 1", METROPOLIS, GOTHAMCITY);

        assertThat(matchResult.hasMatches()).isEqualTo(true);
    }*/

    private MatchResult runTest(String keyword, String location, String organisationLocation) {
        var aaoRule = makeAaoRule(location);
        var aaoConfig = makeAaoConfig();
        var rule = new KeywordAndLocationMatchRule(aaoRule, aaoConfig);
        var alertContext = new AlertContext(keyword, LocalTime.now(), 1L, location, organisationLocation);
        return rule.match(alertContext);
    }


    private AaoOrganisationConfiguration makeAaoConfig() {
        var aaoConfig = new AaoOrganisationConfiguration();

        ArrayList<Location> locations = new ArrayList<>();
        var gotham = new Location();
        gotham.setName(GOTHAMCITY);
        gotham.setUniqueId(gotham.getName());
        locations.add(gotham);
        var otherLocations = new Location();
        otherLocations.setName("Other Locations");
        otherLocations.setUniqueId("1");
        locations.add(otherLocations);
        aaoConfig.setLocations(locations);
        ArrayList<CatalogKeywordInput> keywords = new ArrayList<>();
        var h1 = new CatalogKeywordInput();
        h1.setKeyword("H 1");
        h1.setUniqueId(h1.getKeyword());
        keywords.add(h1);
        aaoConfig.setKeywords(keywords);
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        var elw = new Vehicle();
        elw.setName("ELW");
        elw.setUniqueId(elw.getName());
        var hlf = new Vehicle();
        hlf.setName("HLF");
        hlf.setUniqueId(hlf.getName());
        var tlf = new Vehicle();
        tlf.setName("TLF");
        tlf.setUniqueId(tlf.getName());
        vehicles.add(elw);
        vehicles.add(hlf);
        vehicles.add(tlf);
        aaoConfig.setVehicles(vehicles);

        return aaoConfig;
    }

    private Aao makeAaoRule(String location) {
        Aao aao = new Aao();
        ArrayList<String> keywords = new ArrayList<>();
        keywords.add("H 1");
        ArrayList<String> locations = new ArrayList<>();
        locations.add(location);
        ArrayList<String> vehicles = new ArrayList<>();
        vehicles.add("ELW");
        vehicles.add("HLF");
        vehicles.add("TLF");
        aao.setKeywords(keywords);
        aao.setLocations(locations);
        aao.setVehicles(vehicles);
        aao.setUniqueId(UUID.randomUUID().toString());
        return aao;
    }
}
