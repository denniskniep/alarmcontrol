package com.alarmcontrol.server.aao;

import com.alarmcontrol.server.aao.config.AaoRule;
import com.alarmcontrol.server.aao.config.AaoOrganisationConfiguration;
import com.alarmcontrol.server.aao.config.Keyword;
import com.alarmcontrol.server.aao.config.Location;
import com.alarmcontrol.server.aao.config.TimeRange;
import com.alarmcontrol.server.aao.config.Vehicle;
import com.alarmcontrol.server.aao.graphql.AaoRuleMutation;
import com.alarmcontrol.server.aao.graphql.CatalogKeywordInput;
import com.alarmcontrol.server.data.OrganisationConfigurationService;
import com.alarmcontrol.server.data.graphql.ClientValidationException;

import java.security.Key;
import java.sql.Time;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AaoConfigurationService {

  private Logger logger = LoggerFactory.getLogger(AaoConfigurationService.class);
  private OrganisationConfigurationService organisationConfigurationService;

  public AaoConfigurationService(OrganisationConfigurationService organisationConfigurationService) {
    this.organisationConfigurationService = organisationConfigurationService;
  }

  public AaoRule addAao(Long organisationId, AaoRule aao) {
    logger.info("Add new AaoRule {} to Organisation {}:\n{}", aao.getUniqueId(), organisationId, aao.toString());
    AaoOrganisationConfiguration config = organisationConfigurationService.loadAaoConfig(organisationId);
    List<AaoRule> aaoRules = config.getAaoRules();
    aaoRules.add(aao);
    config.setAaoRules(aaoRules);

    organisationConfigurationService.saveAaoConfig(organisationId, config);

    return aao;
  }

  public AaoRule editAao(Long organisationId, AaoRule aao) {
    logger.info("Edit exisitng AaoRule {} of Organisation {}:\n{}", aao.getUniqueId(), organisationId, aao.toString());
    AaoOrganisationConfiguration config = organisationConfigurationService.loadAaoConfig(organisationId);

    List<AaoRule> aaoRules = config.getAaoRules();
    Optional<AaoRule> aaoToEdit = aaoRules
        .stream()
        .filter(c -> StringUtils.equals(c.getUniqueId(), aao.getUniqueId())).findFirst();

    if (!aaoToEdit.isPresent()) {
      throw new ClientValidationException("No aao found for id:" + aao.getUniqueId());
    }

    aaoToEdit.get().setKeywords(aao.getKeywords());
    aaoToEdit.get().setLocations(aao.getLocations());
    aaoToEdit.get().setTimeRangeNames(aao.getTimeRangeNames());
    aaoToEdit.get().setVehicles(aao.getVehicles());
    config.setAaoRules(aaoRules);

    organisationConfigurationService.saveAaoConfig(organisationId, config);

    return aao;
  }

  public Vehicle addVehicle(long organisationId, Vehicle vehicle) {
    logger.info("Add vehicle {} to Organisation {}:\n{}", vehicle.getUniqueId(), organisationId, vehicle.toString());
    AaoOrganisationConfiguration config = organisationConfigurationService.loadAaoConfig(organisationId);

    List<Vehicle> vehicles = config.getVehicles();
    vehicles.add(vehicle);
    config.setVehicles(vehicles);

    organisationConfigurationService.saveAaoConfig(organisationId, config);

    return vehicle;
  }

  public String deleteVehicle(Long organisationId, String uniqueVehicleId) {
    logger.info("Delete vehicle {} from Organisation {}", uniqueVehicleId, organisationId);
    AaoOrganisationConfiguration config = organisationConfigurationService.loadAaoConfig(organisationId);

    // TODO Delete all associated aao rules and show a warning message before!
    // TODO Alternative: Remove deleted vehicle from all associated rules
    List<Vehicle> vehicles = config.getVehicles();

    List<Vehicle> vehiclesToDelete = vehicles
        .stream()
        .filter(c -> StringUtils.equals(c.getUniqueId(), uniqueVehicleId))
        .collect(Collectors.toList());

    if(vehiclesToDelete.isEmpty()){
      logger.warn("No vehicles with id {} found in Organisation {} for delete", uniqueVehicleId, organisationId);
    }

    for (Vehicle vehicle : vehiclesToDelete) {
      vehicles.remove(vehicle);
      logger.info("Removed vehicle {} from Organisation {}:\n{}", uniqueVehicleId, organisationId, vehicle.toString());
    }
    config.setVehicles(vehicles);

    organisationConfigurationService.saveAaoConfig(organisationId, config);

    return uniqueVehicleId;
  }

  public Location addLocation(Long organisationId, Location location) {
    logger.info("Add location {} to Organisation {}:\n{}", location.getUniqueId(), organisationId, location.toString());
    AaoOrganisationConfiguration config = organisationConfigurationService.loadAaoConfig(organisationId);

    List<Location> locations = config.getLocations();
    locations.add(location);
    config.setLocations(locations);

    organisationConfigurationService.saveAaoConfig(organisationId, config);

    return location;
  }


  public String deleteLocation(Long organisationId, String uniqueLocationId) {
    logger.info("Delete location {} from Organisation {}", uniqueLocationId, organisationId);
    AaoOrganisationConfiguration config = organisationConfigurationService.loadAaoConfig(organisationId);

    // TODO Delete all associated aao rules and show a warning message before!
    // TODO Alternative: Remove deleted vehicle from all associated rules
    List<Location> locations = config.getLocations();

    List<Location> locationsToDelete = locations
        .stream()
        .filter(c -> StringUtils.equals(c.getUniqueId(), uniqueLocationId))
        .collect(Collectors.toList());

    if(locationsToDelete.isEmpty()){
      logger.warn("No locations with id {} found in Organisation {} for delete", uniqueLocationId, organisationId);
    }

    for (Location location : locationsToDelete) {
      locations.remove(location);
      logger.info("Removed location {} from Organisation {}:\n{}", uniqueLocationId, organisationId, location.toString());
    }
    config.setLocations(locations);

    organisationConfigurationService.saveAaoConfig(organisationId, config);

    return uniqueLocationId;
  }

  public String deleteAao(Long organisationId, String uniqueAaoId) {
    logger.info("Delete AaoRule {} from Organisation {}", uniqueAaoId, organisationId);
    AaoOrganisationConfiguration config = organisationConfigurationService.loadAaoConfig(organisationId);

    List<AaoRule> aaoRules = config.getAaoRules();
    List<AaoRule> aaoRulesToDelete = aaoRules
        .stream()
        .filter(c -> StringUtils.equals(c.getUniqueId(), uniqueAaoId))
        .collect(Collectors.toList());

    if(aaoRulesToDelete.isEmpty()){
      logger.warn("No AaoRule with id {} found in Organisation {} for delete", uniqueAaoId, organisationId);
    }

    for (AaoRule aaoRule : aaoRulesToDelete) {
      aaoRules.remove(aaoRule);
      logger.info("Removed AaoRule {} from Organisation {}:\n{}", uniqueAaoId, organisationId, aaoRule.toString());
    }
    config.setAaoRules(aaoRules);

    organisationConfigurationService.saveAaoConfig(organisationId, config);
    return uniqueAaoId;
  }

  public List<Keyword> addOrReplaceCatalogKeywords(Long organisationId, List<CatalogKeywordInput> keywords) {
    logger.info("Add or Replace CatalogKeywords to Organisation {}", organisationId);

    AaoOrganisationConfiguration config = organisationConfigurationService.loadAaoConfig(organisationId);

    // TODO Add consistency check. Replacement of all alarm keywords could be problematic if depended aao rules exists
    List<Keyword> keywordsConfig = config.getKeywords();
    keywordsConfig.clear();
    List<Keyword> keywordsToAdd = keywords
        .stream()
        .map(k -> new Keyword(k.getUniqueId(), k.getKeyword()))
        .collect(Collectors.toList());
    keywordsConfig.addAll(keywordsToAdd);

    organisationConfigurationService.saveAaoConfig(organisationId, config);
    return keywordsToAdd;
  }

  public TimeRange addTimeRange(Long organisationId, TimeRange timeRange) {
    logger.info("Add TimeRange {} to Organisation {}:\n{}", timeRange.getUniqueId(), organisationId, timeRange.toString());

    timeRange.validateFromTime();
    timeRange.validateToTime();

    AaoOrganisationConfiguration config = organisationConfigurationService.loadAaoConfig(organisationId);

    List<TimeRange> timeRanges = config.getTimeRanges();
    timeRanges.add(timeRange);
    config.setTimeRanges(timeRanges);

    organisationConfigurationService.saveAaoConfig(organisationId, config);

    return timeRange;
  }

  public String deleteTimeRange(Long organisationId, String uniqueId) {
    logger.info("Delete TimeRange {} from Organisation {}", uniqueId, organisationId);
    AaoOrganisationConfiguration config = organisationConfigurationService.loadAaoConfig(organisationId);

    // TODO Consider Relations to AAO Rule
    List<TimeRange> timeRanges = config.getTimeRanges();

    List<TimeRange> toDelete = timeRanges
        .stream()
        .filter(t -> StringUtils.equals(t.getUniqueId(), uniqueId))
        .collect(Collectors.toList());

    if(toDelete.isEmpty()){
      logger.warn("No TimeRange with id {} found in Organisation {} for delete", uniqueId, organisationId);
    }

    for (TimeRange timeRange : toDelete) {
      timeRanges.remove(timeRange);
      logger.info("Removed TimeRange {} from Organisation {}:\n{}", uniqueId, organisationId, timeRange.toString());
    }

    config.setTimeRanges(timeRanges);

    organisationConfigurationService.saveAaoConfig(organisationId, config);

    return uniqueId;
  }
}
