package com.alarmcontrol.server.logging.logbook;

import static org.zalando.logbook.Origin.REMOTE;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.zalando.logbook.BaseHttpMessage;

@ConfigurationProperties(prefix = "logbook")
public class LogbookCustomProperties {

  private final LogbookFilterProperties inbound = new LogbookFilterProperties();
  private final LogbookFilterProperties outbound = new LogbookFilterProperties();

  public LogbookFilterProperties getInbound() {
    return inbound;
  }

  public LogbookFilterProperties getOutbound() {
    return outbound;
  }

  public LogbookFilterProperties getFilterProperties(final BaseHttpMessage message) {
    if (message.getOrigin() == REMOTE) {
      return this.getInbound();
    }
    return this.getOutbound();
  }
}

