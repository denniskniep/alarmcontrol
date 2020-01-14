package com.alarmcontrol.server.data.graphql.alert;

import com.alarmcontrol.server.data.models.Alert;
import com.alarmcontrol.server.data.repositories.AlertRepository;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
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

  public PaginatedAlerts alertsByOrganisationId(Long organisationId, int page, int size) {
    Pageable sortedByDateDesc = PageRequest.of(page, size,
        Sort.by("utcDateTime")
            .descending().and(
            Sort.by("id")
                .descending()));

    if (organisationId == null) {
      Page<Alert> all = alertRepository.findAll(sortedByDateDesc);
      return new PaginatedAlerts(
          all.getTotalElements(),
          all.get().collect(Collectors.toList()));
    }

    Page<Alert> byOrganisationId = alertRepository.findByOrganisationId(organisationId, sortedByDateDesc);
    return new PaginatedAlerts(
        byOrganisationId.getTotalElements(),
        byOrganisationId.get().collect(Collectors.toList()));
  }
}
