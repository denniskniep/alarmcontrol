package com.alarmcontrol.server.data.graphql.alert;

import com.alarmcontrol.server.data.models.Alert;
import com.coxautodev.graphql.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

@Component
public class AddressRawFieldAtAlertResolver implements GraphQLResolver<Alert> {

  public String addressRaw(Alert alert) {
    return alert.getAddress();
  }
}
