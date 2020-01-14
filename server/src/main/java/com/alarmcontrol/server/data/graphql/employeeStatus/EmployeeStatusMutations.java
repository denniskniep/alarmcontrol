package com.alarmcontrol.server.data.graphql.employeeStatus;

import com.alarmcontrol.server.data.EmployeeFeedbackService;
import com.alarmcontrol.server.data.EmployeeStatusService;
import com.alarmcontrol.server.data.graphql.employeeFeedback.EmployeeFeedback;
import com.alarmcontrol.server.data.models.EmployeeStatus;
import com.alarmcontrol.server.data.models.Feedback;
import com.alarmcontrol.server.data.models.Status;
import com.alarmcontrol.server.data.repositories.EmployeeStatusRepository;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class EmployeeStatusMutations implements GraphQLMutationResolver {

  private EmployeeStatusService employeeStatusService;

  public EmployeeStatusMutations(EmployeeStatusService employeeStatusService) {
    this.employeeStatusService = employeeStatusService;
  }

  public EmployeeStatus addEmployeeStatus(Long organisationId,
      String employeeReferenceId,
      Status status,
      Date utcDateTime) {
    return employeeStatusService.addEmployeeStatus(organisationId, employeeReferenceId, status, utcDateTime, "");
  }
}
