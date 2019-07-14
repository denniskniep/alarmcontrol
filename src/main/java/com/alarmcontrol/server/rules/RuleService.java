package com.alarmcontrol.server.rules;

import com.alarmcontrol.server.data.models.OrganisationConfiguration;
import com.alarmcontrol.server.data.repositories.OrganisationConfigurationRepository;
import com.alarmcontrol.server.rules.data.RuleContainerData;
import com.alarmcontrol.server.rules.data.RuleContainerDataCollection;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class RuleService {
    private Logger logger = LoggerFactory.getLogger(RuleService.class);
    public static final String AAO_KEY = "AAO_RULESET";
    private OrganisationConfigurationRepository organisationConfigurationRepository;

    public RuleService(OrganisationConfigurationRepository organisationConfigurationRepository) {
        this.organisationConfigurationRepository = organisationConfigurationRepository;
    }

    public MatchResult evaluateAao(AlertContext alertContext) {
        var oid = alertContext.getOrganisationId();
        var foundOrganisationConfiguration = this.organisationConfigurationRepository.findByOrganisationIdAndKey(oid, AAO_KEY);
        if (foundOrganisationConfiguration.isEmpty()) {
            throw new RuntimeException("No configuration for AAO and organisationid: '" + oid + "' found.");
        }
        var organisationConfiguration = foundOrganisationConfiguration.get();
        RuleContainerDataCollection ruleContainerDataCollection;
        try {
            ruleContainerDataCollection = new ObjectMapper().readValue(organisationConfiguration.getValue(), RuleContainerDataCollection.class);
        } catch (IOException e) {
            throw new RuntimeException("Error during 'RuleContainerDataCollection' deserialisation for AAO", e);
        }

        MatchResult globalMatchResult = new MatchResult(new ArrayList<>());
        for (var ruleContainer : ruleContainerDataCollection.getRuleContainers()) {
            ArrayList<Rule> rules = new ArrayList<>();
            for (var ruleData : ruleContainer.getRules()) {
                rules.add(ruleData.create());
            }
            var evaluator = new RuleEvaluator(rules, ruleContainer.getResults());
            var matchResult = evaluator.match(alertContext);
            globalMatchResult.addDistinct(matchResult);
        }
        return globalMatchResult;
    }

    public void saveAaoRules(Long organisationId,String json){
        try {
            new ObjectMapper().readValue(json, RuleContainerDataCollection.class);
        } catch (IOException e) {
            throw new RuntimeException("Error during 'RuleContainerDataCollection' deserialisation for AAO", e);
        }
        OrganisationConfiguration organisationConfiguration;
        var foundOrganisationConfiguration = this.organisationConfigurationRepository.findByOrganisationIdAndKey(organisationId, AAO_KEY);
        if (foundOrganisationConfiguration.isEmpty()) {
            organisationConfiguration  = new OrganisationConfiguration(organisationId,AAO_KEY,json);
        }
        else {
            organisationConfiguration = foundOrganisationConfiguration.get();
            organisationConfiguration.setValue(json);
        }
        organisationConfigurationRepository.save(organisationConfiguration);
    }

    public void save(Long organisationId, ArrayList<RuleContainerData> ruleContainerData) {
        String json;
        try {
            json = new ObjectMapper().writeValueAsString(new RuleContainerDataCollection(ruleContainerData));
        } catch (IOException e) {
            throw new RuntimeException("Error during 'RuleContainerDataCollection' deserialisation for AAO", e);
        }
        saveAaoRules(organisationId, json);
    }
}
