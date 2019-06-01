package com.alarmcontrol.server.data.repositories;

import com.alarmcontrol.server.data.models.Skill;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface SkillRepository extends CrudRepository<Skill, Long> {
  List<Skill> findByOrganisationId(Long organisationId);
}
