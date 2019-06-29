package com.alarmcontrol.server.data.graphql.employeeFeedback;

import com.alarmcontrol.server.data.EmployeeFeedbackService;
import com.alarmcontrol.server.data.models.Feedback;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class EmployeeFeedbackMutations implements GraphQLMutationResolver {

  private EmployeeFeedbackService employeeFeedbackService;

  public EmployeeFeedbackMutations(EmployeeFeedbackService employeeFeedbackService) {
    this.employeeFeedbackService = employeeFeedbackService;
  }

  public EmployeeFeedback addEmployeeFeedback(Long organisationId,
      String alertCallReferenceId,
      String employeeReferenceId,
      Feedback feedback,
      Date dateTime) {
    return employeeFeedbackService
        .addEmployeeFeedback(organisationId, alertCallReferenceId, employeeReferenceId, feedback, dateTime);
  }
}
