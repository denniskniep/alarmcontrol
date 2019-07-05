package com.alarmcontrol.server.data.graphql.alertCall;

import com.alarmcontrol.server.data.AlertService;
import com.alarmcontrol.server.data.graphql.ClientValidationException;
import com.alarmcontrol.server.data.models.AlertCall;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
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
      String address,
      String description) {

    if (StringUtils.isBlank(alertNumber)) {
      throw new ClientValidationException("alertNumber should not be blank");
    }

    if (StringUtils.isBlank(alertReferenceId)) {
      throw new ClientValidationException("alertReferenceId should not be blank");
    }

    if (StringUtils.isBlank(alertCallReferenceId)) {
      throw new ClientValidationException("alertCallReferenceId should not be blank");
    }

    if (StringUtils.isBlank(keyword)) {
      throw new ClientValidationException("keyword should not be blank");
    }

    return alertService
        .create(organisationId,
            alertNumber,
            alertReferenceId,
            alertCallReferenceId,
            keyword,
            dateTime,
            address,
            description,
            null);
  }
}
