package com.alarmcontrol.server.aao.ruleengine;

import com.alarmcontrol.server.aao.config.Aao;
import com.alarmcontrol.server.aao.config.AaoOrganisationConfiguration;
import com.alarmcontrol.server.aao.config.Keyword;
import com.alarmcontrol.server.aao.config.Location;
import com.alarmcontrol.server.aao.config.Vehicle;
import com.alarmcontrol.server.aao.ruleengine.rules.AaoRule;
import com.alarmcontrol.server.aao.ruleengine.rules.KeywordRule;
import com.alarmcontrol.server.aao.ruleengine.rules.LocationRule;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public class RuleEvaluator {
  private AaoOrganisationConfiguration aaoConfig;

  public RuleEvaluator(AaoOrganisationConfiguration aaoConfig) {
    this.aaoConfig = aaoConfig;
  }

  public MatchResult match(ReferenceContext referenceContext, AlertContext alertContext) {
      //ToDo: Wurden Spezialfahrzeug SUBs alarmiert?
      // Fahrzeuge hinzufügen

      //ToDo: Wurden nicht Spezialfahrzeug SUBs alarmiert?
      // Zu den Spezialfahrzeugen addieren
      var aaoRuleConfigs = aaoConfig.getAaoRules();
      for(var aaoRuleConfig : aaoRuleConfigs) {
        List<AaoRule> rules = createRules(aaoRuleConfig);
        if(rules.stream().allMatch(r -> r.match(referenceContext, alertContext))){
          List<Vehicle> vehicles = resolveVehicleIds(aaoRuleConfig.getVehicles());
          List<String> vehicleNames = vehicles
              .stream()
              .map(v -> v.getName())
              .collect(Collectors.toList());
          return new MatchResult(vehicleNames);
        }
      }

      return new MatchResult();
    }

  private List<Vehicle> resolveVehicleIds(ArrayList<String> vehicleIds) {
    List<Vehicle> resolvedVehicles = new ArrayList<>();
    for (String vehicleId : vehicleIds) {
      Optional<Vehicle> foundVehicle = aaoConfig
          .getVehicles()
          .stream()
          .filter(k -> StringUtils.equals(k.getUniqueId(), vehicleId))
          .findFirst();

      if(foundVehicle.isPresent()){
        resolvedVehicles.add(foundVehicle.get());
      }
    }

    return resolvedVehicles;
  }

  private List<AaoRule> createRules(Aao aao){
    List<AaoRule> rules = new ArrayList<>();

    if(aao.getKeywords() != null) {
      rules.add(new KeywordRule(resolveKeywordIds(aao.getKeywords())));
    }

    if(aao.getLocations() != null) {
      rules.add(new LocationRule(resolveLocationIds(aao.getLocations())));
    }

    return rules;
  }

  private List<Keyword> resolveKeywordIds(ArrayList<String> keywordIds) {
    List<Keyword> resolvedKeywords = new ArrayList<>();
    for (String keywordId : keywordIds) {
      Optional<Keyword> foundKeyword = aaoConfig
          .getKeywords()
          .stream()
          .filter(k -> StringUtils.equals(k.getUniqueId(), keywordId))
          .findFirst();

      if(foundKeyword.isPresent()){
        resolvedKeywords.add(foundKeyword.get());
      }
    }

    return resolvedKeywords;
  }

  private List<Location> resolveLocationIds(ArrayList<String> locationIds) {
    List<Location> referenceLocations = new ArrayList<>(aaoConfig.getLocations());
    Location ownLocation = new Location();
    ownLocation.setUniqueId(LocationRule.OwnOrganisationUniqueKey);
    referenceLocations.add(ownLocation);

    Location otherLocation = new Location();
    otherLocation.setUniqueId(LocationRule.OtherOrganisationsUniqueKey);
    referenceLocations.add(otherLocation);

    List<Location> resolvedLocations = new ArrayList<>();
    for (String locationId : locationIds) {
      Optional<Location> foundLocation = referenceLocations
          .stream()
          .filter(l -> StringUtils.equals(l.getUniqueId(), locationId))
          .findFirst();

      if(foundLocation.isPresent()){
        resolvedLocations.add(foundLocation.get());
      }
    }

    return resolvedLocations;
  }
}