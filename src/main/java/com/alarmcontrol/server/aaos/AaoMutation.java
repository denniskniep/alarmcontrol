package com.alarmcontrol.server.aaos;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class AaoMutation implements GraphQLMutationResolver {

    private AaoConfigurationService aaoConfigurationService;

    public AaoMutation(AaoConfigurationService aaoConfigurationService) {
        this.aaoConfigurationService = aaoConfigurationService;
    }

    public Aao addAao(Long organisationId, List<String> keywords, List<String> locations, List<String> vehicles) {
        Aao aao = createAao(UUID.randomUUID().toString(), keywords, locations, vehicles);
        return aaoConfigurationService.addAao(organisationId, aao);
    }

    public Aao editAao(Long organisationId,String uniqueAaoId, List<String> keywords, List<String> locations, List<String> vehicles) {
        Aao aao = createAao(uniqueAaoId, keywords, locations, vehicles);
        return aaoConfigurationService.editAao(organisationId, aao);
    }

    @NotNull
    private Aao createAao(String uniqueAaoId, List<String> keywords, List<String> locations, List<String> vehicles) {
        Aao aao = new Aao();
        aao.setUniqueId(uniqueAaoId);
        aao.setKeywords(new ArrayList<>(keywords));
        aao.setVehicles(new ArrayList<>(vehicles));
        aao.setLocations(new ArrayList<>(locations));
        return aao;
    }
}

