package com.alarmcontrol.server.data.repositories;

import com.alarmcontrol.server.data.models.AlertCall;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface AlertCallRepository extends CrudRepository<AlertCall, Long> {
  List<AlertCall> findByAlertId(Long alertId);
  Optional<AlertCall> findByOrganisationIdAndReferenceId(Long organisationId, String referenceId);

}
