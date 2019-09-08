package com.alarmcontrol.server.data.graphql.alert;

import org.springframework.stereotype.Component;
import com.alarmcontrol.server.data.AlertService;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;

@Component
public class AlertMutations implements GraphQLMutationResolver {

  private AlertService alertService;

  public AlertMutations(AlertService alertService) {
    this.alertService = alertService;
  }

  public Long deleteAlert(Long id){
    alertService.delete(id);
    return id;
  }
}
