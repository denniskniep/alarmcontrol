package com.alarmcontrol.server.aao.ruleengine;

import java.util.Date;

public class AlertContext {
    private String keyword;
    private Date utcDateTime;
    private String geocodedAlertLocation;

    public AlertContext(String keyword, Date utcDateTime, String geocodedAlertLocation) {
        this.keyword = keyword;
        this.utcDateTime = utcDateTime;
        this.geocodedAlertLocation = geocodedAlertLocation;
    }

    public Date getUtcDateTime() {
        return utcDateTime;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getGeocodedAlertLocation() { return geocodedAlertLocation; }
}
