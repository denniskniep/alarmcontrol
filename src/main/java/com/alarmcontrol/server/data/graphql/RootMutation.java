package com.alarmcontrol.server.data.graphql;

import com.alarmcontrol.server.data.AlertService;
import com.alarmcontrol.server.data.models.Alert;
import com.alarmcontrol.server.data.models.Organisation;
import com.alarmcontrol.server.data.repositories.OrganisationRepository;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class RootMutation implements GraphQLMutationResolver {

  private AlertService alertService;
  private OrganisationRepository organisationRepository;

  public RootMutation(AlertService alertService,
      OrganisationRepository organisationRepository) {
    this.alertService = alertService;
    this.organisationRepository = organisationRepository;
  }

  public Alert newAlert(Long organisationId,
      String keyword,
      Date dateTime,
      String description,
      String address) {
    return alertService.create(organisationId, keyword, dateTime, description, address);
  }

  public Organisation newOrganisation(String name, String addressLat, String addressLng) {
    Organisation org = new Organisation(name, addressLat, addressLng);
    organisationRepository.save(org);
    return org;
  }
}