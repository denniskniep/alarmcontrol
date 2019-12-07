package com.alarmcontrol.server.aao;

import com.alarmcontrol.server.aao.config.AaoOrganisationConfiguration;
import com.alarmcontrol.server.aao.ruleengine.AlertContext;
import com.alarmcontrol.server.aao.ruleengine.Feiertag;
import com.alarmcontrol.server.aao.ruleengine.FeiertagService;
import com.alarmcontrol.server.aao.ruleengine.MatchResult;
import com.alarmcontrol.server.aao.ruleengine.ReferenceContext;
import com.alarmcontrol.server.aao.ruleengine.RuleEvaluator;
import com.alarmcontrol.server.data.OrganisationConfigurationService;
import com.alarmcontrol.server.data.repositories.OrganisationRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AaoRuleService {

  private Logger logger = LoggerFactory.getLogger(AaoRuleService.class);

  private OrganisationConfigurationService organisationConfigurationService;
  private OrganisationRepository organisationRepository;
  private FeiertagService feiertagService;

  public AaoRuleService(OrganisationConfigurationService organisationConfigurationService,
      OrganisationRepository organisationRepository,
      FeiertagService feiertagService) {
    this.organisationConfigurationService = organisationConfigurationService;
    this.organisationRepository = organisationRepository;
    this.feiertagService = feiertagService;
  }

  public MatchResult evaluateAao(long organisationId, AlertContext alertContext) {
    var organisation = organisationRepository.findById(organisationId).get();

    AaoOrganisationConfiguration aaoConfig = organisationConfigurationService
        .loadAaoConfig(organisationId);

    List<Feiertag> feiertage = feiertagService.getFeiertage();
    ReferenceContext referenceContext = new ReferenceContext(feiertage, organisation.getLocation());
    //ToDo: Add Error if there is no FeiertagConfig for this year
    //ToDo: Add Warn if there is no FeiertagConfig for next year

    if(aaoConfig.getAaoRules().isEmpty()){
      logger.info("There are no AAO Rules for organisationId {}", organisationId);
      return new MatchResult();
    }

    RuleEvaluator ruleEvaluator = new RuleEvaluator(aaoConfig);
    return ruleEvaluator.match(referenceContext, alertContext);
  }
}
