package com.alarmcontrol.server.aao.graphql;

import com.alarmcontrol.server.aao.config.AaoRule;
import com.alarmcontrol.server.aao.AaoConfigurationService;
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

  public AaoRule addAao(Long organisationId, List<String> keywords, List<String> locations, List<String> vehicles) {
    AaoRule aao = createAao(UUID.randomUUID().toString(), keywords, locations, vehicles);
    return aaoConfigurationService.addAao(organisationId, aao);
  }

  public AaoRule editAao(Long organisationId, String uniqueAaoId, List<String> keywords, List<String> locations,
      List<String> vehicles) {
    AaoRule aao = createAao(uniqueAaoId, keywords, locations, vehicles);
    return aaoConfigurationService.editAao(organisationId, aao);
  }

  public String deleteAao(Long organisationId, String uniqueAaoId) {
    return aaoConfigurationService.deleteAao(organisationId, uniqueAaoId);
  }

  @NotNull
  private AaoRule createAao(String uniqueAaoId, List<String> keywords, List<String> locations, List<String> vehicles) {
    AaoRule aao = new AaoRule();
    aao.setUniqueId(uniqueAaoId);
    aao.setKeywords(new ArrayList<>(keywords));
    aao.setVehicles(new ArrayList<>(vehicles));
    aao.setLocations(new ArrayList<>(locations));
    return aao;
  }
}

