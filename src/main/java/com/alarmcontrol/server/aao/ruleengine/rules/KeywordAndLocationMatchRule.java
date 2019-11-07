package com.alarmcontrol.server.aao.ruleengine.rules;

import com.alarmcontrol.server.aao.config.Aao;
import com.alarmcontrol.server.aao.config.Location;
import com.alarmcontrol.server.aao.config.AaoOrganisationConfiguration;
import com.alarmcontrol.server.aao.ruleengine.AlertContext;
import com.alarmcontrol.server.aao.ruleengine.MatchResult;
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
        if(aao.getKeywords().size() == 0){
          return true;
        }

        return aaoConfig.getKeywords().stream()
                .filter(k -> isSameKeyword(alertContext.getKeyword(), k.getKeyword()))
                .anyMatch(k -> aao.getKeywords().contains(k.getUniqueId()));
    }

  private boolean isSameKeyword(String keywordA, String keywordB) {
    String keywordATrimmed = StringUtils.replace(keywordA, " ", "");
    String keywordBTrimmed = StringUtils.replace(keywordB, " ", "");
    return StringUtils.equalsIgnoreCase(keywordATrimmed, keywordBTrimmed);
  }

  @NotNull
    private boolean anyLocationMatches(AlertContext alertContext) {
        var ownLocation = createOrganisationLocation(alertContext.getOrganisationLocation());

        // If the address can not be geocoded we assume that it is an alarm in the organisations location
        var alertLocation = StringUtils.isBlank(alertContext.getGeocodedAlertLocation()) ?
            alertContext.getOrganisationLocation() : alertContext.getGeocodedAlertLocation();

        if (isNoLocationSpecifiedInAao(aao.getLocations()) ||
            isAlertAtOtherLocationThanOrganisationAndInAao(alertLocation, ownLocation) ||
            isAlertAtOrganisationLocationAndInAao(alertLocation, ownLocation)) {
            return true;
        }

        return isAlertAtAnySpecifiedLocationInAao(alertLocation);
    }

  private boolean isNoLocationSpecifiedInAao(List<String> locations) {
    return locations.size() == 0;
  }

  private boolean isAlertAtAnySpecifiedLocationInAao(String alertLocation) {
    return aaoConfig.getLocations().stream()
                .filter(location -> aao.getLocations().contains(location.getUniqueId()) && StringUtils
                    .equalsIgnoreCase(alertLocation, location.getName()))
                .collect(Collectors.toList()).size() > 0;
  }

  private boolean isAlertAtOrganisationLocationAndInAao(String alertLocation, Location ownLocation){
      return aao.getLocations().contains(OwnOrganisationUniqueKey) &&
          ownLocation != null &&
          StringUtils.isNotBlank(ownLocation.getName()) &&
          StringUtils.equals(alertLocation, ownLocation.getName());
    }

    private boolean isAlertAtOtherLocationThanOrganisationAndInAao(String alertLocation, Location ownLocation){
      return aao.getLocations().contains(OtherOrganisationsUniqueKey) &&
          ownLocation != null &&
          StringUtils.isNotBlank(ownLocation.getName()) &&
          !StringUtils.equals(alertLocation, ownLocation.getName());
    }

  private Location createOrganisationLocation(String organisationLocation) {
        var location = new Location();
        location.setUniqueId(OwnOrganisationUniqueKey);
        location.setName(organisationLocation);
        return location;
    }
}
