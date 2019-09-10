package com.alarmcontrol.server.data.graphql.organisation;

import com.alarmcontrol.server.data.models.Organisation;
import com.alarmcontrol.server.data.repositories.OrganisationRepository;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class OrganisationQueries implements GraphQLQueryResolver {

  private OrganisationRepository organisationRepository;

  public OrganisationQueries(OrganisationRepository organisationRepository) {
    this.organisationRepository = organisationRepository;
  }

  public Iterable<Organisation> organisations() {
    return organisationRepository.findAll();
  }

  public Iterable<Organisation> organisationsById(Long organisationId) {
    if(organisationId == null){
      return organisations();
    }

    ArrayList<Organisation> organisations = new ArrayList<>();
    Organisation organisation = organisationById(organisationId);
    if(organisation != null){
      organisations.add(organisation);
    }
    return organisations;
  }

  public Organisation organisationById(Long organisationId) {
    Optional<Organisation> organisation = organisationRepository.findById(organisationId);
    if (organisation.isPresent()) {
      return organisation.get();
    }
    return null;
  }
}
