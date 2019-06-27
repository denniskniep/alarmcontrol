package com.alarmcontrol.server.api;

import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Parameter {

  private static Logger logger = LoggerFactory.getLogger(Parameter.class);
  private String key;
  private String value;

  public Parameter(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }

  public Parameter expectNumber() {
    try {
      Long.valueOf(getValue());
    } catch (NumberFormatException e) {
      logger.warn("Expected parameter '" + getKey() + "' to be a number, but was '" + getValue() + "'");
    }
    return this;
  }

  public Long asLong() {
    try {
      return Long.valueOf(getValue());
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Expected parameter '" + getKey() + "' to be a number, but was '" + getValue() + "'");
    }
  }

  public static Parameter getRequired(Map<String, String> parameter, String key) {
    Optional<String> foundKey = parameter
        .keySet()
        .stream()
        .filter(k -> StringUtils.equalsIgnoreCase(k, key))
        .findFirst();

    if (foundKey.isEmpty()) {
      throw new IllegalArgumentException("Parameter does not contain required key '" + key + "'!");
    }

    String value = parameter.get(foundKey.get());
    if (StringUtils.isBlank(value)) {
      throw new IllegalArgumentException("Parameter '" + key + "' is blank!");
    }

    logger.info("Extracted parameter '" + key + "' with value '" + value + "'");
    return new Parameter(key, value);
  }

  public static String asString(Parameter... params) {
    StringBuilder sb = new StringBuilder();
    for (Parameter param : params) {
      sb.append(param.getKey());
      sb.append(":\n");
      sb.append(param.getValue());
      sb.append("\n");
      sb.append("\n");
    }
    return sb.toString();
  }
}

