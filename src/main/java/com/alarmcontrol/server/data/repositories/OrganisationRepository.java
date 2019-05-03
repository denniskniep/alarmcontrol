package com.alarmcontrol.server.data.repositories;

import com.alarmcontrol.server.data.models.Organisation;
import org.springframework.data.repository.CrudRepository;

public interface OrganisationRepository extends CrudRepository<Organisation, Long> {

}
