package com.alarmcontrol.server.logging;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.MDC;

public class MDCValues implements AutoCloseable {

  private List<String> keys;

  public MDCValues() {
    keys = new ArrayList<>();
  }

  public MDCValues(String key, Object value) {
    this();
    put(key, value);
  }

  public MDCValues put(String key, Object value) {
    try {
      keys.add(key);
      String valueAsString;
      if (value == null) {
        valueAsString = "null";
      } else {
        valueAsString = value.toString();
      }

      MDC.put(key, valueAsString);
    } catch (Exception e) {
      close();
      throw e;
    }
    return this;
  }

  @Override
  public void close() {
    for (String key : keys) {
      MDC.remove(key);
    }
  }
}
