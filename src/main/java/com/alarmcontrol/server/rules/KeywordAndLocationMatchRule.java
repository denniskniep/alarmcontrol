package com.alarmcontrol.server.rules;

import com.alarmcontrol.server.aaos.Aao;
import com.alarmcontrol.server.aaos.CatalogKeywordInput;
import com.alarmcontrol.server.aaos.Location;
import com.alarmcontrol.server.aaos.Vehicle;
import com.alarmcontrol.server.notifications.core.config.AaoOrganisationConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class KeywordAndLocationMatchRule implements AaoRule {

    private Aao aao;
    private AaoOrganisationConfiguration aaoConfig;
    public static final String OwnOrganisationUniqueKey = "286c1e42-14bb-4620-afcb-eeb9869877a0";
    public static final String OtherOrganisationsUniqueKey = "0522cb63-9553-4429-8ac8-614923d590b6";

    public KeywordAndLocationMatchRule(Aao aao, AaoOrganisationConfiguration aaoConfig) {
        this.aao = aao;
        this.aaoConfig = aaoConfig;
    }

    @Override
    public MatchResult match(AlertContext alertContext) {
        var matchResult = new MatchResult();
        if (anyLocationMatches(alertContext) && anyKeywordMatches(alertContext)) {
            matchResult.addDistinct(new MatchResult(new ArrayList<>(getAaoVehicles())));
        }

        return matchResult;
    }

    @NotNull
    private List<String> getAaoVehicles() {
        var vehicles = aaoConfig.getVehicles().stream()
                            .filter(v -> aao.getVehicles().contains(v.getUniqueId()))
                            .collect(Collectors.toList());
        vehicles.sort(Comparator.comparingInt(o -> aao.getVehicles().indexOf(o.getUniqueId())));
        return vehicles.stream().map(m -> m.getName()).collect(Collectors.toList());
    }

    @NotNull
    private boolean anyKeywordMatches(AlertContext alertContext) {
        var entry =  aaoConfig.getKeywords().stream()
                .filter(k -> StringUtils.equals(alertContext.getKeyword(), k.getKeyword()))
                .findFirst();

        return aao.getKeywords().contains(entry.get().getUniqueId());
    }

    @NotNull
    private boolean anyLocationMatches(AlertContext alertContext) {
        var locations = aaoConfig.getLocations();
        var ownLocation = createOrganisationLocation(alertContext.getOrganisationLocation());
        locations.add(ownLocation);
        var alertLocation = alertContext.getGeocodedAlertLocation();

        if ((aao.getLocations().contains(OtherOrganisationsUniqueKey) && !StringUtils.equals(alertLocation, ownLocation.getName())) ||
            (aao.getLocations().contains(OwnOrganisationUniqueKey) && StringUtils.equals(alertLocation, ownLocation.getName()))) {
            return true;
        }

        return locations.stream()
                    .filter(location -> aao.getLocations().contains(location.getUniqueId()) && StringUtils.equals(alertLocation, location.getName()))
                    .collect(Collectors.toList()).size() > 0;
    }


    private Location createOrganisationLocation(String organisationLocation) {
        var location = new Location();
        location.setUniqueId(OwnOrganisationUniqueKey);
        location.setName(organisationLocation);
        return location;
    }
}
