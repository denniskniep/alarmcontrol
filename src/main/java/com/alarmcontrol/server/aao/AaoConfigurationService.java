package com.alarmcontrol.server.aao;

import com.alarmcontrol.server.aao.config.AaoRule;
import com.alarmcontrol.server.aao.config.AaoOrganisationConfiguration;
import com.alarmcontrol.server.aao.config.Keyword;
import com.alarmcontrol.server.aao.config.Location;
import com.alarmcontrol.server.aao.config.TimeRange;
import com.alarmcontrol.server.aao.config.Vehicle;
import com.alarmcontrol.server.aao.graphql.CatalogKeywordInput;
import com.alarmcontrol.server.data.OrganisationConfigurationService;
import com.alarmcontrol.server.data.graphql.ClientValidationException;

import java.sql.Time;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class AaoConfigurationService {

    private OrganisationConfigurationService organisationConfigurationService;

    public AaoConfigurationService(OrganisationConfigurationService organisationConfigurationService) {
        this.organisationConfigurationService = organisationConfigurationService;
    }


    public AaoRule addAao(Long organisationId, AaoRule aao){
        AaoOrganisationConfiguration config = organisationConfigurationService.loadAaoConfig(organisationId);
        List<AaoRule> aaoRules = config.getAaoRules();
        aaoRules.add(aao);
        config.setAaoRules(aaoRules);

        organisationConfigurationService.saveAaoConfig(organisationId, config);

        return aao;
    }

    public AaoRule editAao(Long organisationId, AaoRule aao){
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
        aaoToEdit.get().setVehicles(aao.getVehicles());
        config.setAaoRules(aaoRules);

        organisationConfigurationService.saveAaoConfig(organisationId, config);

        return aao;
    }

    public Vehicle addVehicle(long organisationId, Vehicle vehicle){
        AaoOrganisationConfiguration config = organisationConfigurationService.loadAaoConfig(organisationId);

        List<Vehicle> vehicles = config.getVehicles();
        vehicles.add(vehicle);
        config.setVehicles(vehicles);

        organisationConfigurationService.saveAaoConfig(organisationId, config);

        return vehicle;
    }

    public String deleteVehicle(Long organisationId, String uniqueVehicleId) {
        AaoOrganisationConfiguration config = organisationConfigurationService.loadAaoConfig(organisationId);

        // TODO Delete all associated aao rules and show a warning message before!
        // TODO Alternative: Remove deleted vehicle from all associated rules
        List<Vehicle> vehicles = config.getVehicles();

        List<Vehicle> vehiclesToDelete = vehicles
                .stream()
                .filter(c -> StringUtils.equals(c.getUniqueId(), uniqueVehicleId))
                .collect(Collectors.toList());

        for (Vehicle vehicle : vehiclesToDelete) {
            vehicles.remove(vehicle);
        }
        config.setVehicles(vehicles);

        organisationConfigurationService.saveAaoConfig(organisationId, config);

        return uniqueVehicleId;
    }

    public Location addLocation(Long organisationId, Location location) {
        AaoOrganisationConfiguration config = organisationConfigurationService.loadAaoConfig(organisationId);

        List<Location> locations = config.getLocations();
        locations.add(location);
        config.setLocations(locations);

        organisationConfigurationService.saveAaoConfig(organisationId, config);

        return location;
    }


    public String deleteLocation(Long organisationId, String uniqueLocationId) {
        AaoOrganisationConfiguration config = organisationConfigurationService.loadAaoConfig(organisationId);

        // TODO Delete all associated aao rules and show a warning message before!
        // TODO Alternative: Remove deleted vehicle from all associated rules
        List<Location> locations = config.getLocations();

        List<Location> locationsToDelete = locations
                .stream()
                .filter(c -> StringUtils.equals(c.getUniqueId(), uniqueLocationId))
                .collect(Collectors.toList());

        for (Location location : locationsToDelete) {
            locations.remove(location);
        }
        config.setLocations(locations);

        organisationConfigurationService.saveAaoConfig(organisationId, config);

        return uniqueLocationId;
    }

    public String deleteAao(Long organisationId, String uniqueAaoId) {
        AaoOrganisationConfiguration config = organisationConfigurationService.loadAaoConfig(organisationId);

        List<AaoRule> aaoRules = config.getAaoRules();
        Optional<AaoRule> aaoToDelete = aaoRules
                .stream()
                .filter(c -> StringUtils.equals(c.getUniqueId(), uniqueAaoId)).findFirst();

        if (!aaoToDelete.isPresent()) {
            throw new ClientValidationException("No aao found for id:" + uniqueAaoId);
        }

        aaoRules.remove(aaoToDelete.get());
        config.setAaoRules(aaoRules);
        organisationConfigurationService.saveAaoConfig(organisationId, config);
        return uniqueAaoId;
    }

    public void addOrReplaceCatalogKeywords(Long organisationId, List<CatalogKeywordInput> keywords) {
        AaoOrganisationConfiguration config = organisationConfigurationService.loadAaoConfig(organisationId);

        // TODO Add consistency check. Replacement of all alarm keywords could be problematic if depended aao rules exists
        List<Keyword> keywordsConfig = config.getKeywords();
        keywordsConfig.clear();
        keywordsConfig.addAll(keywords
            .stream()
            .map(k -> new Keyword(k.getUniqueId(), k.getKeyword()))
            .collect(Collectors.toList()));

        organisationConfigurationService.saveAaoConfig(organisationId, config);
    }

  public TimeRange addTimeRange(Long organisationId, TimeRange timeRange) {
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
    AaoOrganisationConfiguration config = organisationConfigurationService.loadAaoConfig(organisationId);

    // TODO Consider Relations to AAO Rule
    List<TimeRange> timeRanges = config.getTimeRanges();

    List<TimeRange> toDelete = timeRanges
        .stream()
        .filter(t -> StringUtils.equals(t.getUniqueId(), uniqueId))
        .collect(Collectors.toList());

    for (TimeRange timeRange : toDelete) {
      timeRanges.remove(timeRange);
    }

    config.setTimeRanges(timeRanges);

    organisationConfigurationService.saveAaoConfig(organisationId, config);

    return uniqueId;
  }
}
