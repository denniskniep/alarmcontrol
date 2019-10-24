package com.alarmcontrol.server.aaos;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class VehicleMutation implements GraphQLMutationResolver {

    private AaoConfigurationService aaoConfigurationService;

    public VehicleMutation(AaoConfigurationService aaoConfigurationService) {
        this.aaoConfigurationService = aaoConfigurationService;
    }

    public Vehicle addVehicle(Long organisationId, String name) {
        Vehicle vehicle = new Vehicle();
        vehicle.setName(name);
        vehicle.setUniqueId(UUID.randomUUID().toString());
        return aaoConfigurationService.addVehicle(organisationId, vehicle);
    }

    public String deleteVehicle(Long organisationId, String uniqueVehicleId) {
        return aaoConfigurationService.deleteVehicle(organisationId, uniqueVehicleId);
    }
}

