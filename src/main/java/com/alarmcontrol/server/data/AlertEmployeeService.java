package com.alarmcontrol.server.data;

import com.alarmcontrol.server.data.models.AlertCallEmployee;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AlertEmployeeService {

  public List<AlertCallEmployee> findByAlertIdAndEmployeeId(Long alertId, Long employeeId) {
    return null;
  }

  public AlertCallEmployee save(AlertCallEmployee alertCallEmployee) {
    return null;
  }

  public List<AlertCallEmployee> findByAlertId(Long id) {
    return null;
  }
}
