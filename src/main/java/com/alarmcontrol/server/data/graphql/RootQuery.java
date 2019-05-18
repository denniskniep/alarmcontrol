package com.alarmcontrol.server.data.graphql;

import com.alarmcontrol.server.data.models.Alert;
import com.alarmcontrol.server.data.repositories.AlertRepository;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class RootQuery implements GraphQLQueryResolver {
  private AlertRepository alertRepository;

  public RootQuery(AlertRepository alertRepository) {
    this.alertRepository = alertRepository;
  }

  public Alert alertById(Long id) {
    Optional<Alert> alert = alertRepository.findById(id);
    if(alert.isPresent()){
      return alert.get();
    }
    return null;
  }

  public Iterable<Alert> alertsByOrganisationId(Long organisationId) {
    return alertRepository.findByOrganisationId(organisationId);
  }
}