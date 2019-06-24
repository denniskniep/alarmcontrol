package com.alarmcontrol.server.data.graphql.alertCall;

import com.alarmcontrol.server.data.models.Alert;
import com.alarmcontrol.server.data.models.AlertCall;
import com.alarmcontrol.server.data.repositories.AlertRepository;
import com.coxautodev.graphql.tools.GraphQLResolver;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class AlertFieldAtAlertCallResolver implements GraphQLResolver<AlertCall> {

  private AlertRepository alertRepository;

  public AlertFieldAtAlertCallResolver(AlertRepository alertRepository) {
    this.alertRepository = alertRepository;
  }

  public Alert alert(AlertCall alertCall) {
    Optional<Alert> alertById = alertRepository.findById(alertCall.getAlertId());
    if (alertById.isPresent()) {
      return alertById.get();
    }
    return null;
  }
}

