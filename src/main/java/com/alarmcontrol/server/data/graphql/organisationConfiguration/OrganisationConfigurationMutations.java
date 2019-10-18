package com.alarmcontrol.server.data.graphql.organisationConfiguration;

//import com.alarmcontrol.server.data.models.Aao;
import com.alarmcontrol.server.rules.RuleService;
import com.alarmcontrol.server.rules.data.RuleContainerData;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class OrganisationConfigurationMutations implements GraphQLMutationResolver {

    private RuleService ruleService;

    public OrganisationConfigurationMutations(RuleService ruleService) {

        this.ruleService = ruleService;
    }

    public Boolean newAao(Long organisationId, String json) {
        this.ruleService.saveAaoRules(organisationId, json);
        return true;
    }

    /*public Aao editAao(Long id, String keywords, String locations, String vehicles){
        return null;
    }*/



}
