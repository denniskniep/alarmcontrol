package com.alarmcontrol.server.data.graphql;

import com.alarmcontrol.server.data.models.Alert;
import com.alarmcontrol.server.data.repositories.AlertRepository;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class RootMutation implements GraphQLMutationResolver {
  private AlertRepository alertRepository;

  public RootMutation(AlertRepository alertRepository) {
    this.alertRepository = alertRepository;
  }

  public Alert newAlert(Long organisationId,
      String raw,
      boolean active,
      String keyword,
      Date dateTime,
      String description,
      String addressType,
      String address) {
    Alert alert = new Alert(organisationId, raw, active, keyword, dateTime, description, addressType, address);
    alertRepository.save(alert);
    return alert;
  }
}