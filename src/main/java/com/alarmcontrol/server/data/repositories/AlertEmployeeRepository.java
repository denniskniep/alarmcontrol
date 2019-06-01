package com.alarmcontrol.server.data.repositories;

import com.alarmcontrol.server.data.models.AlertEmployee;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface AlertEmployeeRepository extends CrudRepository<AlertEmployee, Long> {
  List<AlertEmployee> findByAlertId(Long alertId);
  List<AlertEmployee> findByAlertIdAndEmployeeId(Long alertId, Long employeeId);
}
