package com.alarmcontrol.server.data.repositories;

import com.alarmcontrol.server.data.models.AlertCallEmployee;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AlertCallEmployeeRepository extends CrudRepository<AlertCallEmployee, Long> {
  List<AlertCallEmployee> findByAlertCallId(Long alertCallId);
  List<AlertCallEmployee> findByAlertCallIdAndEmployeeId(Long alertCallId, Long employeeId);

  @Query(value = "SELECT ace FROM AlertCallEmployee ace "
                + "JOIN AlertCall ac "
                + "on ace.alertCallId = ac.id "
                + "WHERE ac.alertId = ?1")
  List<AlertCallEmployee> findByAlertId(Long alertId);
}
