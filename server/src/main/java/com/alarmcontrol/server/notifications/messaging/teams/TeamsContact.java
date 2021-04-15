package com.alarmcontrol.server.notifications.messaging.teams;

import com.alarmcontrol.server.notifications.core.config.Contact;

public class TeamsContact implements Contact {

    private String uniqueId;
    private String name;
    private String url;

    @Override
    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "TeamsContact{" +
                "uniqueId='" + uniqueId + '\'' +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}