package com.alarmcontrol.server.data.graphql.alert;

import com.alarmcontrol.server.data.models.Alert;
import com.alarmcontrol.server.data.models.AlertCall;
import com.alarmcontrol.server.data.repositories.AlertCallRepository;
import com.coxautodev.graphql.tools.GraphQLResolver;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AlertCallsFieldAtAlertResolver implements GraphQLResolver<Alert> {

  private AlertCallRepository alertCallRepository;

  public AlertCallsFieldAtAlertResolver(AlertCallRepository alertCallRepository) {
    this.alertCallRepository = alertCallRepository;
  }

  public List<AlertCall> alertCalls(Alert alert) {
    return alertCallRepository.findByAlertId(alert.getId());
  }
}
