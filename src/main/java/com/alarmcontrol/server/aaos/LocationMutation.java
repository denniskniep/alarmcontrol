package com.alarmcontrol.server.aaos;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class LocationMutation implements GraphQLMutationResolver {

    private AaoConfigurationService aaoConfigurationService;

    public LocationMutation(AaoConfigurationService aaoConfigurationService) {
        this.aaoConfigurationService = aaoConfigurationService;
    }

    public Location addLocation(Long organisationId, String name) {
        Location location = new Location();
        location.setName(name);
        location.setUniqueId(UUID.randomUUID().toString());
        return aaoConfigurationService.addLocation(organisationId, location);
    }

    public String deleteLocation(Long organisationId, String uniqueLocationId) {
        return aaoConfigurationService.deleteLocation(organisationId, uniqueLocationId);
    }
}
