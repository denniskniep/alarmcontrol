package com.alarmcontrol.server.rules;

import java.time.LocalTime;

public class AlertContext {
    private String keyword;
    private LocalTime localTime;
    private Long organisationId;
    private String geocodedAlertLocation;
    private String organisationLocation;


    public AlertContext(String keyword, LocalTime localTime, Long organisationId, String geocodedAlertLocation, String organisationLocation) {
        this.keyword = keyword;
        this.localTime = localTime;
        this.organisationId = organisationId;
        this.geocodedAlertLocation = geocodedAlertLocation;
        this.organisationLocation = organisationLocation;
    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    public String getKeyword() {
        return keyword;
    }

    public Long getOrganisationId() {
        return organisationId;
    }

    public String getGeocodedAlertLocation() { return geocodedAlertLocation; }

    public String getOrganisationLocation() {
        return organisationLocation;
    }
}
