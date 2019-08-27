package com.alarmcontrol.server.logging.logbook;

import java.util.List;
import org.zalando.logbook.BaseHttpRequest;

public class Conditions {

  public static boolean isRelevant(BaseHttpRequest request, List<String> include, List<String> exclude) {
    if (exclude
        .stream()
        .anyMatch(p -> org.zalando.logbook.Conditions.requestTo(p)
            .test(request))) {
      return false;
    }

    if (include
        .isEmpty()) {
      return true;
    }

    if (include
        .stream()
        .anyMatch(p -> org.zalando.logbook.Conditions.requestTo(p)
            .test(request))) {
      return true;
    }

    return false;
  }
}
