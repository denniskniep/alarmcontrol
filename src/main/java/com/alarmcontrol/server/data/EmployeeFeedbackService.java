package com.alarmcontrol.server.data;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.maxBy;

import com.alarmcontrol.server.data.graphql.employeeFeedback.EmployeeFeedback;
import com.alarmcontrol.server.data.graphql.employeeFeedback.publisher.EmployeeFeedbackAddedPublisher;
import com.alarmcontrol.server.data.models.Alert;
import com.alarmcontrol.server.data.models.AlertCall;
import com.alarmcontrol.server.data.models.AlertCallEmployee;
import com.alarmcontrol.server.data.models.Employee;
import com.alarmcontrol.server.data.models.Feedback;
import com.alarmcontrol.server.data.repositories.AlertCallEmployeeRepository;
import com.alarmcontrol.server.data.repositories.AlertCallRepository;
import com.alarmcontrol.server.data.repositories.AlertRepository;
import com.alarmcontrol.server.data.repositories.EmployeeRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class EmployeeFeedbackService {

  private AlertRepository alertRepository;
  private AlertCallRepository alertCallRepository;
  private AlertCallEmployeeRepository alertCallEmployeeRepository;
  private EmployeeFeedbackAddedPublisher employeeFeedbackAddedPublisher;
  private EmployeeRepository employeeRepository;

  public EmployeeFeedbackService(
      AlertRepository alertRepository,
      AlertCallRepository alertCallRepository,
      AlertCallEmployeeRepository alertCallEmployeeRepository,
      EmployeeFeedbackAddedPublisher employeeFeedbackAddedPublisher,
      EmployeeRepository employeeRepository) {
    this.alertRepository = alertRepository;
    this.alertCallRepository = alertCallRepository;
    this.alertCallEmployeeRepository = alertCallEmployeeRepository;
    this.employeeFeedbackAddedPublisher = employeeFeedbackAddedPublisher;
    this.employeeRepository = employeeRepository;
  }

  public EmployeeFeedback addEmployeeFeedback(Long organisationId,
      String alertCallReferenceId,
      String employeeReferenceId,
      Feedback feedback,
      Date dateTime) {
    return addEmployeeFeedback(organisationId, alertCallReferenceId, employeeReferenceId, feedback, dateTime, null);
  }

  public EmployeeFeedback addEmployeeFeedback(Long organisationId,
      String alertCallReferenceId,
      String employeeReferenceId,
      Feedback feedback,
      Date dateTime,
      String raw){

    if (dateTime == null) {
      dateTime = new Date();
    }

    Optional<AlertCall> foundAlertCall = alertCallRepository
        .findByOrganisationIdAndReferenceId(organisationId, alertCallReferenceId);

    if (foundAlertCall.isEmpty()) {
      throw new IllegalArgumentException("No AlertCall found for referenceId '" + alertCallReferenceId + "'"
          + " in organisationId '" + organisationId + "'");
    }

    Optional<Employee> foundEmployee = employeeRepository
        .findByOrganisationIdAndReferenceId(organisationId, employeeReferenceId);

    if (foundEmployee.isEmpty()) {
      throw new IllegalArgumentException("No Employee found for referenceId '" + employeeReferenceId + "'"
          + " in organisationId '" + organisationId + "'");
    }

    AlertCallEmployee alertCallEmployee = new AlertCallEmployee(foundEmployee.get().getId(),
        foundAlertCall.get().getId(), feedback, "", dateTime);

    alertCallEmployeeRepository.save(alertCallEmployee);

    employeeFeedbackAddedPublisher.emitEmployeeFeedbackForAlertAdded(foundAlertCall.get().getAlertId(),
        alertCallEmployee.getAlertCallId(),
        alertCallEmployee.getEmployeeId());

    return new EmployeeFeedback(alertCallEmployee.getEmployeeId(),
        alertCallEmployee.getFeedback(),
        alertCallEmployee.getDateTime());
  }

  /**
   * Returns the EmployeeFeedback for the Alert
   *
   * It includes the EmployeeFeedback for all related AlertCalls, but returns only the latest feedback and ignores
   * previous sent Feedback from the employee
   *
   * If there is no Feedback yet from the employee the response will include Feedback.NO_RESPONSE
   */
  public List<EmployeeFeedback> findByAlertId(Long alertId) {

    Optional<Alert> foundAlert = alertRepository.findById(alertId);
    if (foundAlert.isEmpty()) {
      throw new IllegalArgumentException("No Alert found for id '" + alertId + "'");
    }

    List<EmployeeFeedback> employeeFeedbacks = alertCallEmployeeRepository.findByAlertId(alertId)
        .stream()
        .map(ace -> new EmployeeFeedback(ace.getEmployeeId(), ace.getFeedback(), ace.getDateTime()))
        .collect(Collectors.toList());

    List<EmployeeFeedback> latestEmployeeFeedback = getLatestEmployeeFeedback(employeeFeedbacks);
    List<EmployeeFeedback> allEmployeeFeedback = addNoResponseIfNoEmployeeFeedbackAlreadyExists(
        foundAlert.get().getOrganisationId(), latestEmployeeFeedback);
    return allEmployeeFeedback;
  }

  private List<EmployeeFeedback> getLatestEmployeeFeedback(List<EmployeeFeedback> employeeFeedbacks) {
    return employeeFeedbacks
        .stream()
        .collect(
            groupingBy(EmployeeFeedbackService::getEmployeeId,
                maxBy(Comparator.comparingLong(EmployeeFeedbackService::getTimeStamp))))
        .values()
        .stream()
        .map(ace -> ace.get())
        .collect(Collectors.toList());
  }

  private static Long getEmployeeId(EmployeeFeedback t) {
    return t.getEmployeeId();
  }

  private static Long getTimeStamp(EmployeeFeedback t) {
    return t.getDateTime().getTime();
  }

  private List<EmployeeFeedback> addNoResponseIfNoEmployeeFeedbackAlreadyExists(Long organisationId,
      List<EmployeeFeedback> existingEmployeeFeedbacks) {

    List<EmployeeFeedback> noResponseEmployeeFeedback = employeeRepository
        .findByOrganisationId(organisationId)
        .stream()
        .filter(e -> !existsIn(existingEmployeeFeedbacks, e.getId()))
        .map(e -> new EmployeeFeedback(e.getId(), Feedback.NO_RESPONSE, null))
        .collect(Collectors.toList());

    List<EmployeeFeedback> allEmployeeFeedbacks = new ArrayList<>();
    allEmployeeFeedbacks.addAll(existingEmployeeFeedbacks);
    allEmployeeFeedbacks.addAll(noResponseEmployeeFeedback);
    return allEmployeeFeedbacks;
  }

  private boolean existsIn(List<EmployeeFeedback> existingEmployeeFeedbacks, Long employeeId) {
    return existingEmployeeFeedbacks.stream().anyMatch(e -> e.getEmployeeId().equals(employeeId));
  }
}
