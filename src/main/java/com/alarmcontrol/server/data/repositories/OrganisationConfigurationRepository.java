package com.alarmcontrol.server.data.repositories;

import com.alarmcontrol.server.data.models.OrganisationConfiguration;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OrganisationConfigurationRepository extends CrudRepository<OrganisationConfiguration, Long> {
    Optional<OrganisationConfiguration> findByOrganisationIdAndKey(Long organisationId, String key);
}
