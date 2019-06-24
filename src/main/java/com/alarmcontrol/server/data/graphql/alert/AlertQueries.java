package com.alarmcontrol.server.data.graphql.alert;

import com.alarmcontrol.server.data.models.Alert;
import com.alarmcontrol.server.data.repositories.AlertRepository;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class AlertQueries implements GraphQLQueryResolver {

  private AlertRepository alertRepository;

  public AlertQueries(AlertRepository alertRepository) {
    this.alertRepository = alertRepository;
  }

  public Alert alertById(Long id) {
    Optional<Alert> alert = alertRepository.findById(id);
    if (alert.isPresent()) {
      return alert.get();
    }
    return null;
  }

  public Iterable<Alert> alertsByOrganisationId(Long organisationId) {
    return alertRepository.findByOrganisationId(organisationId);
  }

  public Iterable<Alert> alertsByOrganisationId(Long organisationId, int page, int size) {
    Pageable sortedByDateDesc = PageRequest.of(page, size,
        Sort.by("dateTime")
            .descending().and(
            Sort.by("id")
                .descending()));
    return alertRepository.findByOrganisationId(organisationId, sortedByDateDesc);
  }
}
