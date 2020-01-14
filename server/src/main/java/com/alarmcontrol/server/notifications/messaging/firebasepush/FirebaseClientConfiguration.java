package com.alarmcontrol.server.notifications.messaging.firebasepush;

public class FirebaseClientConfiguration {
  private String apiKey;
  private String projectId;
  private String messagingSenderId;
  private String appId;
  private String pagerUrl;

  public FirebaseClientConfiguration(String apiKey,
      String projectId,
      String messagingSenderId,
      String appId,
      String pagerUrl) {
    this.apiKey = apiKey;
    this.projectId = projectId;
    this.messagingSenderId = messagingSenderId;
    this.appId = appId;
    this.pagerUrl = pagerUrl;
  }

  public String getApiKey() {
    return apiKey;
  }

  public String getProjectId() {
    return projectId;
  }

  public String getMessagingSenderId() {
    return messagingSenderId;
  }

  public String getAppId() {
    return appId;
  }

  public String getPagerUrl() {
    return pagerUrl;
  }
}
