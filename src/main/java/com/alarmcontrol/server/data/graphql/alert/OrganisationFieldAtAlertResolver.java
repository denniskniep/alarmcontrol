package com.alarmcontrol.server.data.graphql.alert;

import com.alarmcontrol.server.data.models.Alert;
import com.alarmcontrol.server.data.models.Organisation;
import com.alarmcontrol.server.data.repositories.OrganisationRepository;
import com.coxautodev.graphql.tools.GraphQLResolver;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class OrganisationFieldAtAlertResolver implements GraphQLResolver<Alert> {

  private OrganisationRepository organisationRepository;

  public OrganisationFieldAtAlertResolver(
      OrganisationRepository organisationRepository) {
    this.organisationRepository = organisationRepository;
  }

  public Organisation organisation(Alert alert) {
    Optional<Organisation> organisation = organisationRepository.findById(alert.getOrganisationId());
    if(organisation.isPresent()){
      return organisation.get();
    }
    return null;
  }
}
