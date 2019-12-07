package com.alarmcontrol.server.aao.ruleengine.rules;

import com.alarmcontrol.server.aao.config.Location;
import com.alarmcontrol.server.aao.ruleengine.AlertContext;
import com.alarmcontrol.server.aao.ruleengine.ReferenceContext;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class LocationRule implements Rule {

  public static final String OwnOrganisationUniqueKey = "286c1e42-14bb-4620-afcb-eeb9869877a0";
  public static final String OtherOrganisationsUniqueKey = "0522cb63-9553-4429-8ac8-614923d590b6";

  private List<Location> locations;

  public LocationRule(List<Location> locations) {
    this.locations = locations;
  }

  @Override
  public boolean match(ReferenceContext referenceContext, AlertContext alertContext) {
    var ownLocation = createOrganisationLocation(referenceContext.getOrganisationLocation());

    // If the address can not be geocoded we assume that it is an alarm in the organisations location
    var alertLocation = StringUtils.isBlank(alertContext.getGeocodedAlertLocation()) ?
        ownLocation.getName() : alertContext.getGeocodedAlertLocation();

    if (isNoLocationSpecified() ||
        isAlertAtOtherLocationThanOrganisationAndInAao(alertLocation, ownLocation) ||
        isAlertAtOrganisationLocationAndInAao(alertLocation, ownLocation)) {
      return true;
    }

    return isAlertAtAnySpecifiedLocationInAao(alertLocation);
  }

  private Location createOrganisationLocation(String organisationLocation) {
    var location = new Location();
    location.setUniqueId(OwnOrganisationUniqueKey);
    location.setName(organisationLocation);
    return location;
  }

  private boolean isNoLocationSpecified() {
    return locations.size() == 0;
  }

  private boolean isAlertAtAnySpecifiedLocationInAao(String alertLocation) {
    return locations
        .stream()
        .anyMatch(l -> StringUtils.equalsIgnoreCase(alertLocation, l.getName()));
  }

  private boolean isAlertAtOrganisationLocationAndInAao(String alertLocation, Location ownLocation) {
    return ownLocation != null &&
        StringUtils.equals(alertLocation, ownLocation.getName()) &&
        locations
            .stream()
            .anyMatch(l -> StringUtils.equals(l.getUniqueId(), ownLocation.getUniqueId()));
  }

  private boolean isAlertAtOtherLocationThanOrganisationAndInAao(String alertLocation, Location ownLocation) {
    return ownLocation != null &&
        !StringUtils.equals(alertLocation, ownLocation.getName()) &&
        locations
            .stream()
            .anyMatch(l -> StringUtils.equals(l.getUniqueId(), OtherOrganisationsUniqueKey));
  }
}
