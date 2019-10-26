package com.alarmcontrol.server.rules;

import java.time.LocalTime;

public class AlertContext {
    private String keyword;
    private LocalTime localTime;
    private Long organisationId;
    private String location;

    public AlertContext(String keyword, LocalTime localTime, Long organisationId, String location) {

        this.keyword = keyword;
        this.localTime = localTime;
        this.organisationId = organisationId;
        this.location = location;
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

    public String getLocation() { return location; }
}
