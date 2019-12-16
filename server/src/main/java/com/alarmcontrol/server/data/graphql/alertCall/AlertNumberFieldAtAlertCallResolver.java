package com.alarmcontrol.server.data.graphql.alertCall;

import com.alarmcontrol.server.data.models.AlertCall;
import com.alarmcontrol.server.data.models.AlertNumber;
import com.alarmcontrol.server.data.repositories.AlertNumberRepository;
import com.coxautodev.graphql.tools.GraphQLResolver;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class AlertNumberFieldAtAlertCallResolver implements GraphQLResolver<AlertCall> {

  private AlertNumberRepository alertNumberRepository;

  public AlertNumberFieldAtAlertCallResolver(
      AlertNumberRepository alertNumberRepository) {
    this.alertNumberRepository = alertNumberRepository;
  }

  public AlertNumber alertNumber(AlertCall alertCall) {
    Optional<AlertNumber> alertNumberById = alertNumberRepository.findById(alertCall.getAlertNumberId());
    if(alertNumberById.isPresent()){
      return alertNumberById.get();
    }
    return null;
  }
}
