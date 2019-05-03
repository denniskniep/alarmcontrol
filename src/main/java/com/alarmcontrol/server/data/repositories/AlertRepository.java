package com.alarmcontrol.server.data.repositories;

import com.alarmcontrol.server.data.models.Alert;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface AlertRepository extends CrudRepository<Alert, Long> {
  List<Alert> findByOrganisationId(Long organisationId);
}

