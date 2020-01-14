package com.alarmcontrol.server.data.graphql.organisation;

import com.alarmcontrol.server.data.models.AlertNumber;
import com.alarmcontrol.server.data.models.Organisation;
import com.alarmcontrol.server.data.repositories.AlertNumberRepository;
import com.coxautodev.graphql.tools.GraphQLResolver;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AlertNumbersFieldAtOrganisationResolver implements GraphQLResolver<Organisation> {

  private AlertNumberRepository alertNumberRepository;

  public AlertNumbersFieldAtOrganisationResolver(
      AlertNumberRepository alertNumberRepository) {
    this.alertNumberRepository = alertNumberRepository;
  }

  public List<AlertNumber> alertNumbers(Organisation organisation) {
    return alertNumberRepository
        .findByOrganisationId(organisation.getId());
  }
}
