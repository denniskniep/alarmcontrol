package com.alarmcontrol.server.aao;

import com.alarmcontrol.server.data.OrganisationConfigurationService;
import com.alarmcontrol.server.aao.config.AaoOrganisationConfiguration;
import com.alarmcontrol.server.aao.ruleengine.AlertContext;
import com.alarmcontrol.server.aao.ruleengine.MatchResult;
import com.alarmcontrol.server.aao.ruleengine.RuleEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AaoRuleService {
    private Logger logger = LoggerFactory.getLogger(AaoRuleService.class);
    private OrganisationConfigurationService organisationConfigurationService;

    // TODO Remove not needed dependencies
    public AaoRuleService( OrganisationConfigurationService organisationConfigurationService) {
        this.organisationConfigurationService = organisationConfigurationService;
    }

    //TODO Remove old aao code(menu bar, classes..)
    //TODO Integrate logic in extendable rules engine
    //TODO Support special cases like "my home town" or "other towns"
    //ToDO Refactor make it nice
    public MatchResult evaluateAao(AlertContext alertContext) {
      AaoOrganisationConfiguration aaoConfig = organisationConfigurationService.loadAaoConfig(alertContext.getOrganisationId());
      RuleEvaluator ruleEvaluator = new RuleEvaluator(aaoConfig);
      return ruleEvaluator.match(alertContext);
    }
}
