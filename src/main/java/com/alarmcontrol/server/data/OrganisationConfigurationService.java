package com.alarmcontrol.server.data;

import com.alarmcontrol.server.data.models.OrganisationConfiguration;
import com.alarmcontrol.server.data.repositories.OrganisationConfigurationRepository;
import com.alarmcontrol.server.notifications.core.config.AaoOrganisationConfiguration;
import com.alarmcontrol.server.notifications.core.config.NotificationOrganisationConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrganisationConfigurationService {

  private Logger logger = LoggerFactory.getLogger(OrganisationConfigurationService.class);

  private OrganisationConfigurationRepository organisationConfigurationRepository;

  public OrganisationConfigurationService(OrganisationConfigurationRepository organisationConfigurationRepository) {
    this.organisationConfigurationRepository = organisationConfigurationRepository;
  }

  public void saveNotificationConfig(Long organisationId, NotificationOrganisationConfiguration notificationConfig) {
    // Do Validation (Uniqueness, self Validate Contact / Notification??, Reference Integrity...)
    // Maybe Seperate Class

    String json;
    try {
      json = new ObjectMapper().writeValueAsString(notificationConfig);
    } catch (IOException e) {
      throw new RuntimeException("Error during serialisation of 'NotificationConfig' during save config", e);
    }

    Optional<OrganisationConfiguration> foundConfig = organisationConfigurationRepository
        .findByOrganisationIdAndKey(organisationId, NotificationOrganisationConfiguration.KEY);

    OrganisationConfiguration config;
    if (foundConfig.isEmpty()) {
      config = new OrganisationConfiguration(organisationId, NotificationOrganisationConfiguration.KEY, json);
    }else{
      config = foundConfig.get();
      config.setValue(json);
    }
    organisationConfigurationRepository.save(config);
  }

  public void saveAaoConfig(Long organisationId, AaoOrganisationConfiguration aaoConfig) {
    // Do Validation (Uniqueness, self Validate Contact / Notification??, Reference Integrity...)
    // Maybe Seperate Class

    String json;
    try {
      json = new ObjectMapper().writeValueAsString(aaoConfig);
    } catch (IOException e) {
      throw new RuntimeException("Error during serialisation of 'AaoConfig' during save config", e);
    }

    Optional<OrganisationConfiguration> foundConfig = organisationConfigurationRepository
            .findByOrganisationIdAndKey(organisationId, AaoOrganisationConfiguration.KEY);

    OrganisationConfiguration config;
    if (foundConfig.isEmpty()) {
      config = new OrganisationConfiguration(organisationId, AaoOrganisationConfiguration.KEY, json);
    }else{
      config = foundConfig.get();
      config.setValue(json);
    }
    organisationConfigurationRepository.save(config);
  }

  public NotificationOrganisationConfiguration loadNotificationConfig(Long organisationId) {
    Optional<OrganisationConfiguration> foundConfig = organisationConfigurationRepository
        .findByOrganisationIdAndKey(organisationId, NotificationOrganisationConfiguration.KEY);

    if (foundConfig.isEmpty()) {
      return new NotificationOrganisationConfiguration();
    }

    String json = foundConfig.get().getValue();
    NotificationOrganisationConfiguration config;
    try {
      return new ObjectMapper().readValue(json, NotificationOrganisationConfiguration.class);
    } catch (IOException e) {
      throw new RuntimeException("Error during deserialisation of 'NotificationConfig' during load config", e);
    }
  }

  public AaoOrganisationConfiguration loadAaoConfig(Long organisationId) {
    Optional<OrganisationConfiguration> foundConfig = organisationConfigurationRepository
            .findByOrganisationIdAndKey(organisationId, AaoOrganisationConfiguration.KEY);

    if (foundConfig.isEmpty()) {
      return new AaoOrganisationConfiguration();
    }

    String json = foundConfig.get().getValue();
    AaoOrganisationConfiguration config;
    try {
      return new ObjectMapper().readValue(json, AaoOrganisationConfiguration.class);
    } catch (IOException e) {
      throw new RuntimeException("Error during deserialisation of 'AaoConfig' during load config", e);
    }
  }

}
