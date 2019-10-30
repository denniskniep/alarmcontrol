package com.alarmcontrol.server.rules;

import com.alarmcontrol.server.aaos.CatalogKeywordInput;
import com.alarmcontrol.server.aaos.Location;
import com.alarmcontrol.server.data.OrganisationConfigurationService;
import com.alarmcontrol.server.data.models.OrganisationConfiguration;
import com.alarmcontrol.server.data.repositories.OrganisationConfigurationRepository;
import com.alarmcontrol.server.notifications.core.config.AaoOrganisationConfiguration;
import com.alarmcontrol.server.rules.data.RuleContainerData;
import com.alarmcontrol.server.rules.data.RuleContainerDataCollection;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RuleService {
    private Logger logger = LoggerFactory.getLogger(RuleService.class);
    public static final String AAO_KEY = "AAO_RULESET";
    private OrganisationConfigurationRepository organisationConfigurationRepository;
    private OrganisationConfigurationService organisationConfigurationService;
    // TODO Remove not needed dependencies
    public RuleService(OrganisationConfigurationRepository organisationConfigurationRepository, OrganisationConfigurationService organisationConfigurationService) {
        this.organisationConfigurationRepository = organisationConfigurationRepository;
        this.organisationConfigurationService = organisationConfigurationService;
    }

    //TODO Remove old aao code(menu bar, classes..)
    //TODO Integrate logic in extendable rules engine
    //TODO Support special cases like "my home town" or "other towns"
    //ToDO Refactor make it nice
    public MatchResult evaluateAao(AlertContext alertContext) {
        AaoOrganisationConfiguration aaoConfig = organisationConfigurationService.loadAaoConfig(alertContext.getOrganisationId());
        var aaoRules = aaoConfig.getAaoRules();
        MatchResult globalMatchResult = new MatchResult(new ArrayList<>());

        for(var aaoRule : aaoRules) {
            var keywordAndLocationRule = new KeywordAndLocationMatchRule(aaoRule, aaoConfig);
            var matchResult = keywordAndLocationRule.match(alertContext);
            globalMatchResult.addDistinct(matchResult);
        }

        return globalMatchResult;
    }

    /*
        Interface
        locationIDs, keywordIds, locations, keywords

        result: matchresult..>aao
     */

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
