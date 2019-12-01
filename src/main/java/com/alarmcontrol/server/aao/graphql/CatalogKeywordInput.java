package com.alarmcontrol.server.aao.graphql;

public class CatalogKeywordInput {
    private String uniqueId;
    private String keyword;

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
    return "CatalogKeywordInput{" +
        "uniqueId='" + uniqueId + '\'' +
        ", keyword='" + keyword + '\'' +
        '}';
  }
}

