package com.alarmcontrol.server.data;

import com.alarmcontrol.server.data.graphql.employeeFeedback.EmployeeFeedback;
import com.alarmcontrol.server.data.models.AlertCallEmployee;
import com.alarmcontrol.server.data.repositories.AlertCallEmployeeRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class EmployeeFeedbackService {

  private AlertCallEmployeeRepository alertCallEmployeeRepository;

  public EmployeeFeedbackService(
      AlertCallEmployeeRepository alertCallEmployeeRepository) {
    this.alertCallEmployeeRepository = alertCallEmployeeRepository;
  }

  public List<AlertCallEmployee> findByAlertIdAndEmployeeId(Long alertId, Long employeeId) {
    return null;
  }

  public AlertCallEmployee save(AlertCallEmployee alertCallEmployee) {
    return null;
  }

  public List<EmployeeFeedback> findByAlertId(Long alertId) {
    List<EmployeeFeedback> employeeFeedbacks = alertCallEmployeeRepository.findByAlertId(alertId).stream()
        .map(ace -> new EmployeeFeedback(ace.getEmployeeId(), ace.getFeedback(), ace.getDateTime()))
        .collect(Collectors.toList());

    return employeeFeedbacks;
  }
}
