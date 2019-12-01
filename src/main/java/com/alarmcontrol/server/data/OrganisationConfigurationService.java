package com.alarmcontrol.server.data;

import com.alarmcontrol.server.data.models.OrganisationConfiguration;
import com.alarmcontrol.server.data.repositories.OrganisationConfigurationRepository;
import com.alarmcontrol.server.aao.config.AaoOrganisationConfiguration;
import com.alarmcontrol.server.notifications.core.config.NotificationOrganisationConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrganisationConfigurationService {

  private Logger logger = LoggerFactory.getLogger(OrganisationConfigurationService.class);

  private OrganisationConfigurationRepository organisationConfigurationRepository;

  public OrganisationConfigurationService(OrganisationConfigurationRepository organisationConfigurationRepository) {
    this.organisationConfigurationRepository = organisationConfigurationRepository;
  }

  public void saveNotificationConfig(Long organisationId, NotificationOrganisationConfiguration notificationConfig) {
    // ToDo: Validation (Uniqueness, self Validate Contact / Notification??, Reference Integrity...)
    saveConfig(organisationId, NotificationOrganisationConfiguration.KEY, notificationConfig);
  }

  public void saveAaoConfig(Long organisationId, AaoOrganisationConfiguration aaoConfig) {
    // ToDo: Validation (Uniqueness, self Validate, Reference Integrity...)
    // Maybe Seperate Class
    saveConfig(organisationId, AaoOrganisationConfiguration.KEY, aaoConfig);
  }

  public NotificationOrganisationConfiguration loadNotificationConfig(Long organisationId) {
    return loadConfig(
        organisationId,
        NotificationOrganisationConfiguration.KEY,
        NotificationOrganisationConfiguration.class,
        () -> new NotificationOrganisationConfiguration());
  }

  public AaoOrganisationConfiguration loadAaoConfig(Long organisationId) {
    return loadConfig(
        organisationId,
        AaoOrganisationConfiguration.KEY,
        AaoOrganisationConfiguration.class,
        () -> new AaoOrganisationConfiguration());
  }

  private <T> void saveConfig(Long organisationId, String configKey, T config) {
    String json;
    try {
      json = new ObjectMapper().writeValueAsString(config);
    } catch (IOException e) {
      throw new RuntimeException("Error during serialisation of '" + config.getClass().getSimpleName() + "'"
          + " during save config", e);
    }

    Optional<OrganisationConfiguration> foundOrgConfig = organisationConfigurationRepository
        .findByOrganisationIdAndKey(organisationId, configKey);

    OrganisationConfiguration orgConfig;
    if (foundOrgConfig.isEmpty()) {
      logger.info("Organisation {} does not have an {}. Creating a new ",
          organisationId, config.getClass().getSimpleName(), config.getClass().getSimpleName());
      orgConfig = new OrganisationConfiguration(organisationId, configKey, json);
    } else {
      logger.info("Organisation {} already have an {}. Overwriting existing ",
          organisationId, config.getClass().getSimpleName(), config.getClass().getSimpleName());
      orgConfig = foundOrgConfig.get();
      orgConfig.setValue(json);
    }
    organisationConfigurationRepository.save(orgConfig);
    logger.info("{} saved for Organisation {}", config.getClass().getSimpleName(), organisationId );
  }

  private <T> T loadConfig(Long organisationId, String configKey, Class<T> clazz, Supplier<T> createNew) {
    Optional<OrganisationConfiguration> foundConfig = organisationConfigurationRepository
        .findByOrganisationIdAndKey(organisationId, configKey);

    if (foundConfig.isEmpty()) {
      logger.info("Organisation {} does not have an {}. Loading default {}", organisationId, clazz.getSimpleName(), clazz.getSimpleName());
      return createNew.get();
    } else {
      logger.info("Loaded {} from Organisation {}.",clazz.getSimpleName(), organisationId);
    }

    String json = foundConfig.get().getValue();
    try {
      return new ObjectMapper().readValue(json, clazz);
    } catch (IOException e) {
      throw new RuntimeException("Error during deserialisation of '" + clazz.getSimpleName() + "' "
          + "of organisation " + organisationId + " during load config\n" + json, e);
    }
  }
}
