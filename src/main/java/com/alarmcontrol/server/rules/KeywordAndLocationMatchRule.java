package com.alarmcontrol.server.rules;

import com.alarmcontrol.server.aaos.Aao;
import com.alarmcontrol.server.aaos.CatalogKeywordInput;
import com.alarmcontrol.server.aaos.Location;
import com.alarmcontrol.server.notifications.core.config.AaoOrganisationConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KeywordAndLocationMatchRule implements AaoRule {

    private Aao aao;
    private AaoOrganisationConfiguration aaoConfig;

    public KeywordAndLocationMatchRule(Aao aao, AaoOrganisationConfiguration aaoConfig) {
        this.aao = aao;
        this.aaoConfig = aaoConfig;
    }

    @Override
    public MatchResult match(AlertContext alertContext) {
        var matchResult = new MatchResult();

        List<Location> matchedLocations = filterLocations(alertContext);
        List<CatalogKeywordInput> matchedKeywords = filterKeywords(alertContext);
        if (matchedLocations.size() > 0 && matchedKeywords.size() > 0) {
            List<String> vehicles = getAaoVehicles();
            matchResult.addDistinct(new MatchResult(new ArrayList<>(vehicles)));
        }

        return matchResult;
    }

    @NotNull
    private List<String> getAaoVehicles() {
        return aaoConfig.getVehicles().stream()
                            .filter(v -> aao.getVehicles().contains(v.getUniqueId()))
                            .map(v -> v.getName())
                            .collect(Collectors.toList());
    }

    @NotNull
    private List<CatalogKeywordInput> filterKeywords(AlertContext alertContext) {
        return aaoConfig.getKeywords().stream()
                        .filter(k -> aao.getKeywords().contains(k.getUniqueId()) && StringUtils.equals(alertContext.getKeyword(), k.getKeyword()))
                        .collect(Collectors.toList());
    }

    @NotNull
    private List<Location> filterLocations(AlertContext alertContext) {
        var locations = aaoConfig.getLocations();
        locations.add(createOrganisationLocation(alertContext.getOrganisationLocation()));
        var alertLocation = alertContext.getGeocodedAlertLocation();

        if (aao.getLocations().contains("1")) {
            return locations.stream()
                    .filter(location -> !aao.getLocations().contains(0) && aao.getLocations().size() > 0)
                    .collect(Collectors.toList());
        }
        return locations.stream()
                    .filter(location -> (aao.getLocations().contains(location.getUniqueId()) && StringUtils.equals(alertLocation, location.getName())) || StringUtils.equals(alertLocation, alertContext.getOrganisationLocation()))
                    .collect(Collectors.toList());
    }


    private Location createOrganisationLocation(String organisationLocation) {
        var location = new Location();
        location.setUniqueId("0");
        location.setName(organisationLocation);
        return location;
    }
}
