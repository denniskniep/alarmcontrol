package com.alarmcontrol.server.aaos;

import com.alarmcontrol.server.data.OrganisationConfigurationService;
import com.alarmcontrol.server.notifications.core.config.*;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class AaoConfigurationService {

    private OrganisationConfigurationService organisationConfigurationService;

    public AaoConfigurationService(OrganisationConfigurationService organisationConfigurationService) {
        this.organisationConfigurationService = organisationConfigurationService;
    }


    public Aao addAao(Long organisationId, Aao aao){
        AaoOrganisationConfiguration config = organisationConfigurationService.loadAaoConfig(organisationId);

        List<Aao> aaoRules = config.getAaoRules();
        aaoRules.add(aao);
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
}
