package com.alarmcontrol.server.data.graphql.employeeStatus.publisher;

import com.alarmcontrol.server.data.graphql.CommonPublisher;
import org.springframework.stereotype.Component;

@Component
public class EmployeeStatusAddedPublisher extends CommonPublisher<EmployeeStatusAdded> {

  public void emitEmployeeStatusAdded(Long employeeId) {
    this.emit(new EmployeeStatusAdded(employeeId));
    logger.info("Notified Subscribers that Employee " + employeeId + " sent status");
  }
}
