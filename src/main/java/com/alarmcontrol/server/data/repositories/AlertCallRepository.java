package com.alarmcontrol.server.data.repositories;

import com.alarmcontrol.server.data.models.Alert;
import com.alarmcontrol.server.data.models.AlertCall;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface AlertCallRepository extends CrudRepository<AlertCall, Long> {
  List<AlertCall> findByAlertId(Long alertId);
}
