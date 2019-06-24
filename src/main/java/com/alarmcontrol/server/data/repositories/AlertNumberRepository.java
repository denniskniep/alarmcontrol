package com.alarmcontrol.server.data.repositories;

import com.alarmcontrol.server.data.models.AlertNumber;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface AlertNumberRepository extends CrudRepository<AlertNumber, Long> {
  List<AlertNumber> findByNumberIgnoreCase(String number);
  List<AlertNumber> findByOrganisationId(Long organisationId);
}
