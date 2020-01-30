package com.alarmcontrol.server.data.graphql.alert;

import com.alarmcontrol.server.data.models.Alert;
import com.coxautodev.graphql.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AaoFieldAtAlertResolver implements GraphQLResolver<Alert> {


  public List<String> aao(Alert alert) {
    return alert.getAao();
  }
}
