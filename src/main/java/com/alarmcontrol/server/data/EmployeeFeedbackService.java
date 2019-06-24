package com.alarmcontrol.server.data;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.maxBy;

import com.alarmcontrol.server.data.graphql.employeeFeedback.EmployeeFeedback;
import com.alarmcontrol.server.data.models.Alert;
import com.alarmcontrol.server.data.models.Feedback;
import com.alarmcontrol.server.data.repositories.AlertCallEmployeeRepository;
import com.alarmcontrol.server.data.repositories.AlertRepository;
import com.alarmcontrol.server.data.repositories.EmployeeRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class EmployeeFeedbackService {

  private AlertRepository alertRepository;
  private AlertCallEmployeeRepository alertCallEmployeeRepository;
  private EmployeeRepository employeeRepository;

  public EmployeeFeedbackService(
      AlertRepository alertRepository,
      AlertCallEmployeeRepository alertCallEmployeeRepository,
      EmployeeRepository employeeRepository) {
    this.alertRepository = alertRepository;
    this.alertCallEmployeeRepository = alertCallEmployeeRepository;
    this.employeeRepository = employeeRepository;
  }

  private static Long getEmployeeId(EmployeeFeedback t) {
    return t.getEmployeeId();
  }

  private static Long getTimeStamp(EmployeeFeedback t) {
    return t.getDateTime().getTime();
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
