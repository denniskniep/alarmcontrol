package com.alarmcontrol.server.logging.logbook;

import com.alarmcontrol.server.logging.MDCValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.Marker;

public class HttpLogger implements Logger {

  private Logger logger;
  private ObjectMapper mapper;

  public HttpLogger(Logger logger, ObjectMapper mapper) {
    this.logger = logger;
    this.mapper = mapper;
  }

  @Override
  public String getName() {
    return logger.getName();
  }

  @Override
  public boolean isTraceEnabled() {
    return logger.isTraceEnabled();
  }

  @Override
  public void trace(String s) {
    log(s, (msg) -> logger.trace(msg));
  }

  @Override
  public boolean isDebugEnabled() {
    return logger.isDebugEnabled();
  }

  @Override
  public void debug(String s) {
    log(s, (msg) -> logger.debug(msg));
  }

  @Override
  public boolean isInfoEnabled() {
    return logger.isInfoEnabled();
  }

  @Override
  public void info(String s) {
    log(s, (msg) -> logger.info(msg));
  }

  @Override
  public boolean isWarnEnabled() {
    return logger.isWarnEnabled();
  }

  @Override
  public void warn(String s) {
    log(s, (msg) -> logger.warn(msg));
  }

  @Override
  public boolean isErrorEnabled() {
    return logger.isErrorEnabled();
  }

  @Override
  public void error(String s) {
    log(s, (msg) -> logger.error(msg));
  }

  public void log(String s, Consumer<String> logWithLevel) {
    try {
      Map<String, Object> object = tryReadAsMap(s);
      if (object == null) {
        logWithLevel.accept(s);
      } else {
        object = resolveNestings(object);
        String message = s;
        if (object.containsKey("message")) {
          message = object.get("message").toString();
          object.put("message", null);
        }
        try (MDCValues mdcValues = new MDCValues()) {
          for (Entry<String, Object> objectEntry : object.entrySet()) {
            mdcValues.put(objectEntry.getKey(), objectEntry.getValue());
          }
          logWithLevel.accept(message);
        }
      }
    }catch (Exception e){
      logWithLevel.accept(s);
    }
  }

  private Map<String, Object> tryReadAsMap(String value){
    try{
      return mapper.readValue(value, Map.class);
    }catch(Exception e){
      return null;
    }
  }

  private Map<String, Object> resolveNestings(Map<String, Object> objectMap) {
    return resolveNestings("", objectMap);
  }

  private Map<String, Object> resolveNestings(String prefix, Map<String, Object> objectMap) {
    Map<String, Object> allValues = new HashMap<>();
    for (Entry<String, Object> objectEntry : objectMap.entrySet()) {
      Map<String, Object> valueAsMap = tryCast(objectEntry.getValue());
      if(valueAsMap == null){
        allValues.put(prefix + objectEntry.getKey(), objectEntry.getValue());
      }else{
        Map<String, Object> nestedMap = resolveNestings(objectEntry.getKey() + ".", valueAsMap);
        allValues.putAll(nestedMap);
      }
    }
    return allValues;
  }

  private Map<String, Object> tryCast(Object value){
    try{
      return (Map<String, Object>)value;
    }catch (Exception e){
      return null;
    }
  }

  /*Following methods are not used by logbook framework!*/
  @Override
  public void trace(String s, Object o) {
    throw new NotImplementedException("");
  }

  @Override
  public void trace(String s, Object o, Object o1) {
    throw new NotImplementedException("");
  }

  @Override
  public void trace(String s, Object... objects) {
    throw new NotImplementedException("");
  }

  @Override
  public void trace(String s, Throwable throwable) {
    throw new NotImplementedException("");
  }

  @Override
  public boolean isTraceEnabled(Marker marker) {
    throw new NotImplementedException("");
  }

  @Override
  public void trace(Marker marker, String s) {
    throw new NotImplementedException("");
  }

  @Override
  public void trace(Marker marker, String s, Object o) {
    throw new NotImplementedException("");
  }

  @Override
  public void trace(Marker marker, String s, Object o, Object o1) {
    throw new NotImplementedException("");
  }

  @Override
  public void trace(Marker marker, String s, Object... objects) {
    throw new NotImplementedException("");
  }

  @Override
  public void trace(Marker marker, String s, Throwable throwable) {
    throw new NotImplementedException("");
  }

  @Override
  public void debug(String s, Object o) {
    throw new NotImplementedException("");
  }

  @Override
  public void debug(String s, Object o, Object o1) {
    throw new NotImplementedException("");
  }

  @Override
  public void debug(String s, Object... objects) {
    throw new NotImplementedException("");
  }

  @Override
  public void debug(String s, Throwable throwable) {
    throw new NotImplementedException("");
  }

  @Override
  public boolean isDebugEnabled(Marker marker) {
    throw new NotImplementedException("");
  }

  @Override
  public void debug(Marker marker, String s) {
    throw new NotImplementedException("");
  }

  @Override
  public void debug(Marker marker, String s, Object o) {
    throw new NotImplementedException("");
  }

  @Override
  public void debug(Marker marker, String s, Object o, Object o1) {
    throw new NotImplementedException("");
  }

  @Override
  public void debug(Marker marker, String s, Object... objects) {
    throw new NotImplementedException("");
  }

  @Override
  public void debug(Marker marker, String s, Throwable throwable) {
    throw new NotImplementedException("");
  }

  @Override
  public void info(String s, Object o) {
    throw new NotImplementedException("");
  }

  @Override
  public void info(String s, Object o, Object o1) {
    throw new NotImplementedException("");
  }

  @Override
  public void info(String s, Object... objects) {
    throw new NotImplementedException("");
  }

  @Override
  public void info(String s, Throwable throwable) {
    throw new NotImplementedException("");
  }

  @Override
  public boolean isInfoEnabled(Marker marker) {
    throw new NotImplementedException("");
  }

  @Override
  public void info(Marker marker, String s) {
    throw new NotImplementedException("");
  }

  @Override
  public void info(Marker marker, String s, Object o) {
    throw new NotImplementedException("");
  }

  @Override
  public void info(Marker marker, String s, Object o, Object o1) {
    throw new NotImplementedException("");
  }

  @Override
  public void info(Marker marker, String s, Object... objects) {
    throw new NotImplementedException("");
  }

  @Override
  public void info(Marker marker, String s, Throwable throwable) {
    throw new NotImplementedException("");
  }

  @Override
  public void warn(String s, Object o) {
    throw new NotImplementedException("");
  }

  @Override
  public void warn(String s, Object... objects) {
    throw new NotImplementedException("");
  }

  @Override
  public void warn(String s, Object o, Object o1) {
    throw new NotImplementedException("");
  }

  @Override
  public void warn(String s, Throwable throwable) {
    throw new NotImplementedException("");
  }

  @Override
  public boolean isWarnEnabled(Marker marker) {
    throw new NotImplementedException("");
  }

  @Override
  public void warn(Marker marker, String s) {
    throw new NotImplementedException("");
  }

  @Override
  public void warn(Marker marker, String s, Object o) {
    throw new NotImplementedException("");
  }

  @Override
  public void warn(Marker marker, String s, Object o, Object o1) {
    throw new NotImplementedException("");
  }

  @Override
  public void warn(Marker marker, String s, Object... objects) {
    throw new NotImplementedException("");
  }

  @Override
  public void warn(Marker marker, String s, Throwable throwable) {
    throw new NotImplementedException("");
  }

  @Override
  public void error(String s, Object o) {
    throw new NotImplementedException("");
  }

  @Override
  public void error(String s, Object o, Object o1) {
    throw new NotImplementedException("");
  }

  @Override
  public void error(String s, Object... objects) {
    throw new NotImplementedException("");
  }

  @Override
  public void error(String s, Throwable throwable) {
    throw new NotImplementedException("");
  }

  @Override
  public boolean isErrorEnabled(Marker marker) {
    throw new NotImplementedException("");
  }

  @Override
  public void error(Marker marker, String s) {
    throw new NotImplementedException("");
  }

  @Override
  public void error(Marker marker, String s, Object o) {
    throw new NotImplementedException("");
  }

  @Override
  public void error(Marker marker, String s, Object o, Object o1) {
    throw new NotImplementedException("");
  }

  @Override
  public void error(Marker marker, String s, Object... objects) {
    throw new NotImplementedException("");
  }

  @Override
  public void error(Marker marker, String s, Throwable throwable) {
    throw new NotImplementedException("");
  }
}
