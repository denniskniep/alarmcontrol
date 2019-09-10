package com.alarmcontrol.server.logging.logbook;

import java.util.ArrayList;
import java.util.List;

public class LogbookFilterProperties {

  private final List<String> include = new ArrayList<>();
  private final List<String> exclude = new ArrayList<>();

  private final List<String> includeRequestBody = new ArrayList<>();
  private final List<String> excludeRequestBody = new ArrayList<>();

  private final List<String> includeResponseBody = new ArrayList<>();
  private final List<String> excludeResponseBody = new ArrayList<>();

  public List<String> getInclude() {
    return include;
  }

  public List<String> getExclude() {
    return exclude;
  }

  public List<String> getIncludeRequestBody() {
    return includeRequestBody;
  }

  public List<String> getExcludeRequestBody() {
    return excludeRequestBody;
  }

  public List<String> getIncludeResponseBody() {
    return includeResponseBody;
  }

  public List<String> getExcludeResponseBody() {
    return excludeResponseBody;
  }
}
