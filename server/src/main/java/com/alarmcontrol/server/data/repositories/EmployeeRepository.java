package com.alarmcontrol.server.data.repositories;

import com.alarmcontrol.server.data.models.Employee;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
  List<Employee> findByOrganisationId(Long organisationId);
  Optional<Employee> findByOrganisationIdAndReferenceId(Long organisationId, String referenceId);
}
