package com.alarmcontrol.server.data;

import com.alarmcontrol.server.data.graphql.alert.publisher.AlertAddedPublisher;
import com.alarmcontrol.server.data.graphql.alert.publisher.AlertChangedPublisher;
import com.alarmcontrol.server.data.models.Alert;
import com.alarmcontrol.server.data.models.AlertCall;
import com.alarmcontrol.server.data.models.AlertNumber;
import com.alarmcontrol.server.data.models.Organisation;
import com.alarmcontrol.server.data.repositories.AlertCallRepository;
import com.alarmcontrol.server.data.repositories.AlertNumberRepository;
import com.alarmcontrol.server.data.repositories.AlertRepository;
import com.alarmcontrol.server.data.repositories.OrganisationRepository;
import com.alarmcontrol.server.maps.Coordinate;
import com.alarmcontrol.server.maps.GeocodingResult;
import com.alarmcontrol.server.maps.RoutingResult;
import com.alarmcontrol.server.maps.graphhopper.routing.GraphhopperRoutingService;
import com.alarmcontrol.server.maps.mapbox.geocoding.MapboxGeocodingService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlertService {

  private Logger logger = LoggerFactory.getLogger(AlertService.class);

  private AlertRepository alertRepository;
  private MapboxGeocodingService geocodingService;
  private GraphhopperRoutingService routingService;
  private OrganisationRepository organisationRepository;
  private AlertAddedPublisher alertAddedPublisher;
  private AlertChangedPublisher alertChangedPublisher;
  private AlertNumberRepository alertNumberRepository;
  private AlertCallRepository alertCallRepository;

  public AlertService(AlertRepository alertRepository,
      MapboxGeocodingService geocodingService,
      GraphhopperRoutingService routingService,
      OrganisationRepository organisationRepository,
      AlertAddedPublisher alertAddedPublisher,
      AlertChangedPublisher alertChangedPublisher,
      AlertNumberRepository alertNumberRepository,
      AlertCallRepository alertCallRepository) {
    this.alertRepository = alertRepository;
    this.geocodingService = geocodingService;
    this.routingService = routingService;
    this.organisationRepository = organisationRepository;
    this.alertAddedPublisher = alertAddedPublisher;
    this.alertChangedPublisher = alertChangedPublisher;
    this.alertNumberRepository = alertNumberRepository;
    this.alertCallRepository = alertCallRepository;
  }

  @Transactional(isolation = Isolation.READ_COMMITTED)
  public AlertCall create(
      Long organisationId,
      String alertNumber,
      String alertReferenceId,
      String alertCallReferenceId,
      String keyword,
      Date dateTime,
      String address) {
    return create(organisationId,
        alertNumber,
        alertReferenceId,
        alertCallReferenceId,
        keyword,
        dateTime,
        address,
        null,
        null);
  }

  @Transactional(isolation = Isolation.READ_COMMITTED)
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

    if (dateTime == null) {
      dateTime = new Date();
    }

    Optional<AlertNumber> foundAlertNumber = alertNumberRepository
        .findByOrganisationIdAndNumberIgnoreCase(organisationId, alertNumber);
    if (foundAlertNumber.isEmpty()) {
      throw new IllegalArgumentException("No AlertNumber found for number '" + alertNumber + "'"
          + " in organisationId '" + organisationId + "'");
    }

    return create(foundAlertNumber.get(), alertReferenceId, alertCallReferenceId, keyword, dateTime, address,
        description, raw);
  }

  private AlertCall create(
      AlertNumber alertNumber,
      String referenceId,
      String referenceCallId,
      String keyword,
      Date dateTime,
      String address,
      String description,
      String raw) {

    Long organisationId = alertNumber.getOrganisationId();

    List<Alert> existingAlerts = alertRepository
        .findByOrganisationIdAndReferenceId(organisationId, referenceId);

    Alert alert;
    boolean alertCreated = false;

    if (existingAlerts.size() == 0) {
      alert = createAlert(organisationId, referenceId, keyword, dateTime, address, description);
      alertCreated = true;
    } else {
      alert = existingAlerts.get(0);
    }

    AlertCall alertCall = createAlertCall(alertNumber, alert, referenceCallId, dateTime, raw);

    if (alertCreated) {
      alertAddedPublisher.emitAlertAdded(alert.getId());
    } else {
      alertChangedPublisher.emitAlertChanged(alert.getId());
    }
    return alertCall;
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
        Coordinate orgCoordinate = getOrgCoordinate(organisationId);
        Coordinate targetCoordinate = geocodedAddress.getCoordinate();
        RoutingResult route = routingService.route(new ArrayList<>(Arrays.asList(orgCoordinate, targetCoordinate)));

        addressInfo1 = geocodedAddress.getAddressInfo1();
        addressInfo2 = geocodedAddress.getAddressInfo2();
        addressLat = geocodedAddress.getCoordinate().getLat();
        addressLng = geocodedAddress.getCoordinate().getLng();
        addressJson = geocodedAddress.getJson();
        routeJson = route.getJson();
        routeDistance = route.getDistance();
        routeDuration = route.getDuration();
      } catch (Exception e) {
        logger.error("Error during geocoding and routing", e);
      }
    } else {
      logger.warn("Skipping geocoding and routing due to address is blank!");
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
        routeDuration);
    alertRepository.save(alert);
    return alert;
  }

  private Coordinate getOrgCoordinate(Long organisationId) {
    Optional<Organisation> orgById = organisationRepository.findById(organisationId);
    if (!orgById.isPresent()) {
      throw new IllegalArgumentException("No organisation found for id '" + orgById + "'");
    }

    Organisation organisation = orgById.get();
    String orgLat = organisation.getAddressLat();
    String orgLng = organisation.getAddressLng();
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
}
