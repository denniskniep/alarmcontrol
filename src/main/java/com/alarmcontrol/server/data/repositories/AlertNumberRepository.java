package com.alarmcontrol.server.data.repositories;

import com.alarmcontrol.server.data.models.AlertNumber;
import org.springframework.data.repository.CrudRepository;

public interface AlertNumberRepository extends CrudRepository<AlertNumber, Long> {

}
