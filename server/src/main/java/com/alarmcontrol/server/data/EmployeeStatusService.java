package com.alarmcontrol.server.data;

import com.alarmcontrol.server.data.graphql.employeeStatus.publisher.EmployeeStatusAddedPublisher;
import com.alarmcontrol.server.data.models.Employee;
import com.alarmcontrol.server.data.models.EmployeeStatus;
import com.alarmcontrol.server.data.models.Status;
import com.alarmcontrol.server.data.repositories.EmployeeRepository;
import com.alarmcontrol.server.data.repositories.EmployeeStatusRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class EmployeeStatusService {

  private EmployeeRepository employeeRepository;
  private EmployeeStatusRepository employeeStatusRepository;
  private EmployeeStatusAddedPublisher employeeStatusAddedPublisher;

  public EmployeeStatusService(EmployeeRepository employeeRepository,
      EmployeeStatusRepository employeeStatusRepository,
      EmployeeStatusAddedPublisher employeeStatusAddedPublisher) {
    this.employeeRepository = employeeRepository;
    this.employeeStatusRepository = employeeStatusRepository;
    this.employeeStatusAddedPublisher = employeeStatusAddedPublisher;
  }

  public EmployeeStatus addEmployeeStatus(Long organisationId,
      String employeeReferenceId,
      Status status,
      Date utcDateTime,
      String raw) {

    if (utcDateTime == null) {
      utcDateTime = new Date();
    }

    Optional<Employee> foundEmployee = employeeRepository
        .findByOrganisationIdAndReferenceId(organisationId, employeeReferenceId);

    if (foundEmployee.isEmpty()) {
      throw new IllegalArgumentException("No Employee found for referenceId '" + employeeReferenceId + "'"
          + " in organisationId '" + organisationId + "'");
    }

    EmployeeStatus employeeStatus = new EmployeeStatus(foundEmployee.get().getId(), status, utcDateTime, "");
    employeeStatusRepository.save(employeeStatus);
    employeeStatusAddedPublisher.emitEmployeeStatusAdded(employeeStatus.getEmployeeId());
    return employeeStatus;
  }

  public EmployeeStatus getEmployeeStatus(Long employeeId){
    List<EmployeeStatus> allStatus = employeeStatusRepository
        .findByEmployeeIdOrderByUtcDateTimeDesc(employeeId);

    if(allStatus.size() == 0){
      return null;
    }

    return allStatus.get(0);
  }

  public EmployeeStatus getEmployeeStatusUntilOrEqual(Long employeeId, Date untilOrEqualUtcDateTime){
    List<EmployeeStatus> allStatus = employeeStatusRepository
        .findByEmployeeIdOrderByUtcDateTimeDesc(employeeId);

    if(allStatus.size() == 0){
      return null;
    }

    Optional<EmployeeStatus> employeeStatus = allStatus.stream()
        .filter(s -> s.getUtcDateTime().before(untilOrEqualUtcDateTime) ||
            s.getUtcDateTime().equals(untilOrEqualUtcDateTime)).findFirst();

    if(employeeStatus.isEmpty()){
      return null;
    }

    return employeeStatus.get();
  }
}
