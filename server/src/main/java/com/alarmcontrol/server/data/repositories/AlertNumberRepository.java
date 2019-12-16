package com.alarmcontrol.server.data.repositories;

import com.alarmcontrol.server.data.models.AlertNumber;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface AlertNumberRepository extends CrudRepository<AlertNumber, Long> {
  Optional<AlertNumber> findByOrganisationIdAndNumberIgnoreCase(Long organisationId, String number);
  List<AlertNumber> findByOrganisationId(Long organisationId);
}
