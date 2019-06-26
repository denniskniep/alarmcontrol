package com.alarmcontrol.server.data.repositories;

import com.alarmcontrol.server.data.models.Alert;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AlertRepository extends PagingAndSortingRepository<Alert, Long> {
  List<Alert> findByOrganisationId(Long organisationId);
  List<Alert> findByOrganisationIdAndReferenceId(Long organisationId, String referenceId);
  List<Alert> findByOrganisationId(Long organisationId, Pageable pageable);
}

