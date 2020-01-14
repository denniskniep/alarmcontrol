package com.alarmcontrol.server.aao.config;

public class Keyword {
  private String uniqueId;
  private String keyword;

  public Keyword() {
  }

  public Keyword(String uniqueId, String keyword) {
    this.uniqueId = uniqueId;
    this.keyword = keyword;
  }

  public String getUniqueId() {
    return uniqueId;
  }

  public void setUniqueId(String uniqueId) {
    this.uniqueId = uniqueId;
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  @Override
  public String toString() {
    return "Keyword{" +
        "uniqueId='" + uniqueId + '\'' +
        ", keyword='" + keyword + '\'' +
        '}';
  }
}
