package com.alarmcontrol.server.data;

import com.alarmcontrol.server.data.graphql.alert.publisher.AlertAddedPublisher;
import com.alarmcontrol.server.data.graphql.alert.publisher.AlertChangedPublisher;
import com.alarmcontrol.server.data.models.Alert;
import com.alarmcontrol.server.data.models.AlertCall;
import com.alarmcontrol.server.data.models.AlertCallEmployee;
import com.alarmcontrol.server.data.models.AlertNumber;
import com.alarmcontrol.server.data.models.Organisation;
import com.alarmcontrol.server.data.repositories.AlertCallEmployeeRepository;
import com.alarmcontrol.server.data.repositories.AlertCallEmployeeRepository;
import com.alarmcontrol.server.data.repositories.AlertCallRepository;
import com.alarmcontrol.server.data.repositories.AlertNumberRepository;
import com.alarmcontrol.server.data.repositories.AlertRepository;
import com.alarmcontrol.server.data.repositories.OrganisationRepository;
import com.alarmcontrol.server.maps.Coordinate;
import com.alarmcontrol.server.maps.GeocodingResult;
import com.alarmcontrol.server.maps.GeocodingService;
import com.alarmcontrol.server.maps.RoutingResult;
import com.alarmcontrol.server.maps.RoutingService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import com.alarmcontrol.server.rules.RuleService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.alarmcontrol.server.rules.AlertContext;
import com.alarmcontrol.server.rules.MatchResult;
import com.alarmcontrol.server.rules.RuleService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlertService {

  private Logger logger = LoggerFactory.getLogger(AlertService.class);

  private RuleService ruleService;
  private AlertRepository alertRepository;
  private GeocodingService geocodingService;
  private RoutingService routingService;
  private OrganisationRepository organisationRepository;
  private AlertAddedPublisher alertAddedPublisher;
  private AlertChangedPublisher alertChangedPublisher;
  private AlertNumberRepository alertNumberRepository;
  private AlertCallRepository alertCallRepository;
  private AlertCallEmployeeRepository alertCallEmployeeRepository;

  public AlertService(RuleService ruleService,
      AlertRepository alertRepository,
      GeocodingService geocodingService,
      RoutingService routingService,
      OrganisationRepository organisationRepository,
      AlertAddedPublisher alertAddedPublisher,
      AlertChangedPublisher alertChangedPublisher,
      AlertNumberRepository alertNumberRepository,
      AlertCallRepository alertCallRepository,
      AlertCallEmployeeRepository alertCallEmployeeRepository) {
    this.ruleService = ruleService;
    this.alertRepository = alertRepository;
    this.geocodingService = geocodingService;
    this.routingService = routingService;
    this.organisationRepository = organisationRepository;
    this.alertAddedPublisher = alertAddedPublisher;
    this.alertChangedPublisher = alertChangedPublisher;
    this.alertNumberRepository = alertNumberRepository;
    this.alertCallRepository = alertCallRepository;
    this.alertCallEmployeeRepository = alertCallEmployeeRepository;
  }

  public AlertCall create(
      Long organisationId,
      String alertNumber,
      String alertReferenceId,
      String alertCallReferenceId,
      String keyword,
      Date dateTime,
      String address,
      String description,
      String raw) {

    if(StringUtils.isBlank(alertReferenceId)){
      throw new IllegalArgumentException("alertReferenceId can not be blank");
    }

    if(StringUtils.isBlank(alertCallReferenceId)){
      throw new IllegalArgumentException("alertCallReferenceId can not be blank");
    }

    if (dateTime == null) {
      dateTime = new Date();
    }

    AlertCallCreated createdAlertCall = createWithinTransaction(organisationId, alertNumber, alertReferenceId,
        alertCallReferenceId, keyword, dateTime, address,
        description, raw);

    if(createdAlertCall == null){
      return null;
    }

    // Its important that this code is outside of the db-transaction. Because of notification
    // is triggered through websocket, but isolation level protects new alert to be read until commit.
    if (createdAlertCall.isAlertCreated()) {
      alertAddedPublisher.emitAlertAdded(createdAlertCall.getAlert().getId(), organisationId);
    } else {
      alertChangedPublisher.emitAlertChanged(createdAlertCall.getAlert().getId());
    }
    return createdAlertCall.getAlertCall();
  }

  @Transactional(isolation = Isolation.READ_COMMITTED)
  protected AlertCallCreated createWithinTransaction(
      Long organisationId,
      String alertNumber,
      String referenceId,
      String referenceCallId,
      String keyword,
      Date dateTime,
      String address,
      String description,
      String raw) {

    Optional<AlertNumber> foundAlertNumber = alertNumberRepository
        .findByOrganisationIdAndNumberIgnoreCase(organisationId, alertNumber);
    if (foundAlertNumber.isEmpty()) {
      logger.info("No AlertNumber found for number '" + alertNumber + "'" + " in organisationId '" + organisationId + "'");
      return null;
    }

    Optional<Alert> foundAlert = alertRepository
        .findByOrganisationIdAndReferenceId(organisationId, referenceId);

    Alert alert;
    boolean alertCreated = false;

    if (foundAlert.isEmpty()) {
      alert = createAlert(organisationId, referenceId, keyword, dateTime, address, description);
      alertCreated = true;
    } else {
      alert = foundAlert.get();
    }

    AlertCall alertCall = createAlertCall(foundAlertNumber.get(), alert, referenceCallId, dateTime, raw);
    return new AlertCallCreated(alertCall, alert, alertCreated);
  }

  private Alert createAlert(Long organisationId,
      String referenceId,
      String keyword,
      Date dateTime,
      String address,
      String description) {

    String addressInfo1 = null;
    String addressInfo2 = null;
    String addressLat = null;
    String addressLng = null;
    String addressJson = null;

    String routeJson = null;
    Double routeDistance = null;
    Integer routeDuration = null;

    if (!StringUtils.isBlank(address)) {
      try {
        GeocodingResult geocodedAddress = geocodingService.geocode(address);
        addressInfo1 = geocodedAddress.getAddressInfo1();
        addressInfo2 = geocodedAddress.getAddressInfo2();
        addressLat = geocodedAddress.getCoordinate().getLat();
        addressLng = geocodedAddress.getCoordinate().getLng();
        addressJson = geocodedAddress.getJson();

        Coordinate orgCoordinate = getOrgCoordinate(organisationId);
        if (orgCoordinate != null) {
          Coordinate targetCoordinate = geocodedAddress.getCoordinate();
          RoutingResult route = routingService.route(new ArrayList<>(Arrays.asList(orgCoordinate, targetCoordinate)));
          routeJson = route.getJson();
          routeDistance = route.getDistance();
          routeDuration = route.getDuration();
        }else {
          logger.warn("Skipping routing due to lat or lng of organisation is blank!");
        }

      } catch (Exception e) {
        logger.error("Error during geocoding and routing", e);
      }
    } else {
      logger.warn("Skipping geocoding and routing due to address is blank!");
    }
    MatchResult aaoMatchResult = new MatchResult();
    try {
        var localTime = toLocalTime(dateTime);
        aaoMatchResult = ruleService.evaluateAao(new AlertContext(keyword,localTime, organisationId));
    } catch(Exception e){
        logger.error("Error during aao evaluation", e);
    }

    Alert alert = new Alert(organisationId,
        referenceId,
        true,
        keyword,
        dateTime,
        description,
        address,
        addressInfo1,
        addressInfo2,
        addressLat,
        addressLng,
        addressJson,
        routeJson,
        routeDistance,
        routeDuration,
        new StringList(aaoMatchResult.getResults()));
    alertRepository.save(alert);
    return alert;
  }

  private static LocalTime toLocalTime(Date date){
    LocalTime time = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalTime();
    return time;
  }

  private Coordinate getOrgCoordinate(Long organisationId) {
    Optional<Organisation> orgById = organisationRepository.findById(organisationId);
    if (!orgById.isPresent()) {
      throw new IllegalArgumentException("No organisation found for id '" + orgById + "'");
    }

    Organisation organisation = orgById.get();
    String orgLat = organisation.getAddressLat();
    String orgLng = organisation.getAddressLng();
    if (StringUtils.isBlank(orgLat) || StringUtils.isBlank(orgLng)) {
      return null;
    }
    return new Coordinate(orgLat, orgLng);
  }

  private AlertCall createAlertCall(AlertNumber alertNumber,
      Alert alert,
      String referenceCallId,
      Date dateTime,
      String raw) {
    AlertCall alertCall = new AlertCall(alert.getId(),
        alertNumber.getOrganisationId(),
        alertNumber.getId(),
        referenceCallId,
        raw,
        dateTime);
    alertCallRepository.save(alertCall);
    return alertCall;
  }

  @Transactional(isolation = Isolation.READ_COMMITTED)
  public void delete(Long id){
    Optional<Alert> foundAlert = alertRepository.findById(id);
    if (!foundAlert.isPresent()) {
      throw new IllegalArgumentException("No Alert found for id:" + id);
    }

    Alert alert = foundAlert.get();
    List<AlertCall> alertCalls = alertCallRepository.findByAlertId(alert.getId());
    List<AlertCallEmployee> alertCallEmployees = alertCallEmployeeRepository.findByAlertId(alert.getId());
    alertCallEmployeeRepository.deleteAll(alertCallEmployees);
    alertCallRepository.deleteAll(alertCalls);
    alertRepository.delete(alert);
  }

  private static class AlertCallCreated {
    private AlertCall alertCall;
    private Alert alert;
    private boolean alertCreated;

    public AlertCallCreated(AlertCall alertCall, Alert alert, boolean alertCreated) {
      this.alertCall = alertCall;
      this.alert = alert;
      this.alertCreated = alertCreated;
    }

    public AlertCall getAlertCall() {
      return alertCall;
    }

    public Alert getAlert() {
      return alert;
    }

    public boolean isAlertCreated() {
      return alertCreated;
    }
  }
}
