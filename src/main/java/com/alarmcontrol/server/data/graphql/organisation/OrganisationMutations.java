package com.alarmcontrol.server.data.graphql.organisation;

import com.alarmcontrol.server.data.graphql.ClientValidationException;
import com.alarmcontrol.server.data.models.Organisation;
import com.alarmcontrol.server.data.repositories.OrganisationRepository;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class OrganisationMutations implements GraphQLMutationResolver {

  private OrganisationRepository organisationRepository;

  public OrganisationMutations(OrganisationRepository organisationRepository) {
    this.organisationRepository = organisationRepository;
  }

  public Organisation newOrganisation(String name, String addressLat, String addressLng, String location) {
    if (StringUtils.isBlank(name)) {
      throw new ClientValidationException("Name should not be blank");
    }

    Organisation org = new Organisation(name, addressLat, addressLng, location);
    organisationRepository.save(org);
    return org;
  }

  public Organisation editOrganisation(Long id, String name, String addressLat, String addressLng, String location) {
    Optional<Organisation> organisationById = organisationRepository.findById(id);
    if (!organisationById.isPresent()) {
      throw new ClientValidationException("No Organisation found for id:" + id);
    }

    if (StringUtils.isBlank(name)) {
      throw new ClientValidationException("Name should not be blank");
    }

    Organisation organisation = organisationById.get();
    organisation.setName(name);
    organisation.setAddressLat(addressLat);
    organisation.setAddressLng(addressLng);
    organisation.setLocation(location);
    organisationRepository.save(organisation);
    return organisation;
  }

  public Long deleteOrganisation(Long id) {
    Optional<Organisation> organisationById = organisationRepository.findById(id);
    if (!organisationById.isPresent()) {
      throw new ClientValidationException("No Organisation found for id:" + id);
    }

    Organisation organisation = organisationById.get();
    organisationRepository.delete(organisation);
    return organisation.getId();
  }
}
