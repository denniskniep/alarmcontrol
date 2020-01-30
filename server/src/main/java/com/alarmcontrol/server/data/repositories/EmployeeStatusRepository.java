package com.alarmcontrol.server.data.repositories;

import com.alarmcontrol.server.data.models.EmployeeStatus;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeStatusRepository extends CrudRepository<EmployeeStatus, Long> {
  List<EmployeeStatus> findByEmployeeIdOrderByUtcDateTimeDesc(Long employeeId);
}