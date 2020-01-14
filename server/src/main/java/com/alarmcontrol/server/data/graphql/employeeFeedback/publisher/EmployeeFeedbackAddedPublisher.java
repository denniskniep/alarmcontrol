package com.alarmcontrol.server.data.graphql.employeeFeedback.publisher;

import com.alarmcontrol.server.data.graphql.CommonPublisher;
import org.springframework.stereotype.Component;

@Component
public class EmployeeFeedbackAddedPublisher extends CommonPublisher<EmployeeFeedbackAdded> {

  public void emitEmployeeFeedbackForAlertAdded(Long alertId, Long alertCallId, Long employeeId) {
    this.emit(new EmployeeFeedbackAdded(alertId, alertCallId, employeeId));
    logger.info("Notified Subscribers that Employee " + employeeId + " sent feedback for AlertCall " + alertCallId);
  }
}
