package com.alarmcontrol.server.aao.ruleengine;

import com.alarmcontrol.server.aao.config.AaoRule;
import com.alarmcontrol.server.aao.config.AaoOrganisationConfiguration;
import com.alarmcontrol.server.aao.config.Keyword;
import com.alarmcontrol.server.aao.config.Location;
import com.alarmcontrol.server.aao.config.TimeRange;
import com.alarmcontrol.server.aao.config.Vehicle;
import com.alarmcontrol.server.aao.ruleengine.rules.KeywordRule;
import com.alarmcontrol.server.aao.ruleengine.rules.LocationRule;
import com.alarmcontrol.server.aao.ruleengine.rules.Rule;
import com.alarmcontrol.server.aao.ruleengine.rules.TimeRangeRule;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuleEvaluator {

  private Logger logger = LoggerFactory.getLogger(RuleEvaluator.class);

  private AaoOrganisationConfiguration aaoConfig;

  public RuleEvaluator(AaoOrganisationConfiguration aaoConfig) {
    this.aaoConfig = aaoConfig;
  }

  public MatchResult match(ReferenceContext referenceContext, AlertContext alertContext) {
      //ToDo: Wurden Spezialfahrzeug SUBs alarmiert?
      // Fahrzeuge hinzufügen

      //ToDo: Wurden nicht Spezialfahrzeug SUBs alarmiert?
      // Zu den Spezialfahrzeugen addieren
      for(var aaoRuleConfig : aaoConfig.getAaoRules()) {
        List<Rule> rules = createRules(aaoRuleConfig);
        if(rules.stream().allMatch(r -> r.match(referenceContext, alertContext))){
          logger.info("Following AAO Rule matches {}", aaoRuleConfig);
          logger.info("Following AAO Rule matches (resolved)\n" +
                "uniqueId={}\n" +
                "keywords={}\n" +
                "locations={}\n" +
                "timeRangeNames={}\n" +
                "vehicles={}",
              aaoRuleConfig.getUniqueId(),
              resolveKeywordIds(aaoRuleConfig.getKeywords()),
              resolveLocationIds(aaoRuleConfig.getLocations()),
              resolveTimeRanges(aaoRuleConfig.getTimeRangeNames()),
              resolveVehicleIds(aaoRuleConfig.getVehicles())
          );

          List<Vehicle> vehicles = resolveVehicleIds(aaoRuleConfig.getVehicles());
          List<String> vehicleNames = vehicles
              .stream()
              .map(v -> v.getName())
              .collect(Collectors.toList());

          return new MatchResult(aaoRuleConfig.getUniqueId(), vehicleNames);
        }
      }

      logger.info("No AAO Rule matches Alert");
      return new MatchResult();
    }

  private List<Vehicle> resolveVehicleIds(ArrayList<String> vehicleIds) {
    if(vehicleIds == null){
      return new ArrayList<>();
    }

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

  private List<Rule> createRules(AaoRule aao){
    List<Rule> rules = new ArrayList<>();

    if(aao.getKeywords() != null) {
      rules.add(new KeywordRule(resolveKeywordIds(aao.getKeywords())));
    }

    if(aao.getLocations() != null) {
      rules.add(new LocationRule(resolveLocationIds(aao.getLocations())));
    }

    if(aao.getTimeRangeNames() != null){
      rules.add(new TimeRangeRule(resolveTimeRanges(aao.getTimeRangeNames())));
    }

    return rules;
  }

  private List<TimeRange> resolveTimeRanges(ArrayList<String> timeRangeNames) {
    if(timeRangeNames == null){
      return new ArrayList<>();
    }

    List<TimeRange> resolvedTimeRanges = new ArrayList<>();
    for (String timeRangeName : timeRangeNames) {
      List<TimeRange> timeRanges = aaoConfig
          .getTimeRanges()
          .stream()
          .filter(t -> StringUtils.equalsIgnoreCase(t.getName(), timeRangeName))
          .collect(Collectors.toList());
      resolvedTimeRanges.addAll(timeRanges);
    }
    return resolvedTimeRanges;
  }

  private List<Keyword> resolveKeywordIds(ArrayList<String> keywordIds) {
    if(keywordIds == null){
      return new ArrayList<>();
    }

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
    if(locationIds == null){
      return new ArrayList<>();
    }

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