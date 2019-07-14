package com.alarmcontrol.server.rules;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class AlertContext {
    private String keyword;
    private LocalTime localTime;
    private Long organisationId;

    public AlertContext(String keyword, LocalTime localTime, Long organisationId) {

        this.keyword = keyword;
        this.localTime = localTime;
        this.organisationId = organisationId;
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
}
