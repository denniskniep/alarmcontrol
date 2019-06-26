package com.alarmcontrol.server.data.graphql.alertCall;

import com.alarmcontrol.server.data.AlertService;
import com.alarmcontrol.server.data.models.AlertCall;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class AlertCallMutations implements GraphQLMutationResolver {

  private AlertService alertService;

  public AlertCallMutations(AlertService alertService) {
    this.alertService = alertService;
  }

  public AlertCall newAlertCall(Long organisationId,
      String alertNumber,
      String alertReferenceId,
      String alertCallReferenceId,
      String keyword,
      Date dateTime,
      String address) {
    return alertService
        .create(organisationId, alertNumber, alertReferenceId, alertCallReferenceId, keyword, dateTime, address);
  }
}
