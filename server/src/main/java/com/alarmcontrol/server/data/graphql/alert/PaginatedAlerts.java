package com.alarmcontrol.server.data.graphql.alert;

import com.alarmcontrol.server.data.graphql.PaginatedResult;
import com.alarmcontrol.server.data.models.Alert;
import java.util.List;

public class PaginatedAlerts extends PaginatedResult<Alert> {

  public PaginatedAlerts(long totalCount, List<Alert> items) {
    super(totalCount, items);
  }
}
