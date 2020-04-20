package com.alarmcontrol.server.data;

import com.alarmcontrol.server.aao.AaoRuleService;
import com.alarmcontrol.server.aao.ruleengine.AlertContext;
import com.alarmcontrol.server.aao.ruleengine.MatchResult;
import com.alarmcontrol.server.data.graphql.DateTimeHelper;
import com.alarmcontrol.server.data.graphql.alert.publisher.AlertAddedPublisher;
import com.alarmcontrol.server.data.graphql.alert.publisher.AlertChangedPublisher;
import com.alarmcontrol.server.data.models.Alert;
import com.alarmcontrol.server.data.models.AlertCall;
import com.alarmcontrol.server.data.models.AlertCallEmployee;
import com.alarmcontrol.server.data.models.AlertNumber;
import com.alarmcontrol.server.data.models.Organisation;
import com.alarmcontrol.server.data.models.StringList;
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
import com.alarmcontrol.server.notifications.core.NotificationService;
import com.alarmcontrol.server.notifications.usecases.alertcreated.AlertCreatedEvent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlertService {

  private Logger logger = LoggerFactory.getLogger(AlertService.class);

  private AaoRuleService ruleService;
  private AlertRepository alertRepository;
  private GeocodingService geocodingService;
  private RoutingService routingService;
  private OrganisationRepository organisationRepository;
  private AlertAddedPublisher alertAddedPublisher;
  private AlertChangedPublisher alertChangedPublisher;
  private AlertNumberRepository alertNumberRepository;
  private AlertCallRepository alertCallRepository;
  private AlertCallEmployeeRepository alertCallEmployeeRepository;
  private NotificationService notificationService;

  @Value("${alert.active.timeFrameInMs}")
  private long alertActiveTimeFrameInMs;

  @Value("${alertCall.active.timeFrameInMs}")
  private long alertCallActiveTimeFrameInMs;

  public AlertService(AaoRuleService ruleService,
      AlertRepository alertRepository,
      GeocodingService geocodingService,
      RoutingService routingService,
      OrganisationRepository organisationRepository,
      AlertAddedPublisher alertAddedPublisher,
      AlertChangedPublisher alertChangedPublisher,
      AlertNumberRepository alertNumberRepository,
      AlertCallRepository alertCallRepository,
      AlertCallEmployeeRepository alertCallEmployeeRepository,
      NotificationService notificationService) {
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
    this.notificationService = notificationService;
  }

  public AlertCall create(
      Long organisationId,
      String alertNumber,
      String alertReferenceId,
      String alertCallReferenceId,
      String keyword,
      Date utcDateTime,
      String address,
      String description,
      String raw) {

    if (StringUtils.isBlank(alertReferenceId)) {
      throw new IllegalArgumentException("alertReferenceId can not be blank");
    }

    if (StringUtils.isBlank(alertCallReferenceId)) {
      throw new IllegalArgumentException("alertCallReferenceId can not be blank");
    }

    if (utcDateTime == null) {
      // TODO: Ensure UTC date --> Check all dates for UTC handling and client must convert utc to localtime!
      utcDateTime = new Date();
    }

    AlertCallCreated createdAlertCall = createWithinTransaction(organisationId, alertNumber, alertReferenceId,
        alertCallReferenceId, keyword, utcDateTime, address,
        description, raw);

    if (createdAlertCall == null) {
      return null;
    }

    // Its important that this code is outside of the db-transaction. Because of notification
    // is triggered through websocket, but isolation level protects new alert to be read until commit.
    if (createdAlertCall.isAlertCreated()) {
      alertAddedPublisher.emitAlertAdded(createdAlertCall.getAlert().getId(), organisationId);
      notificationService.sendNotifications(new AlertCreatedEvent(createdAlertCall.getAlert()));
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
      Date utcDateTime,
      String address,
      String description,
      String raw) {

    Optional<AlertNumber> foundAlertNumber = alertNumberRepository
        .findByOrganisationIdAndNumberIgnoreCase(organisationId, alertNumber);
    if (foundAlertNumber.isEmpty()) {
      logger.info(
          "No AlertNumber found for number '" + alertNumber + "'" + " in organisationId '" + organisationId + "'");
      return null;
    }

    ExistingAlert existingAlert = createAlertIfNotExists(organisationId,
        referenceId,
        keyword,
        utcDateTime,
        address,
        description);

    if (existingAlert.isCreated()) {
      processAlert(existingAlert.getAlert());
    }

    AlertCall alertCall = createAlertCall(foundAlertNumber.get(),
        existingAlert.getAlert(),
        referenceCallId,
        utcDateTime,
        raw);

    return new AlertCallCreated(alertCall, existingAlert.getAlert(), existingAlert.isCreated);
  }

  public Optional<AlertCall> findActiveAlertCall(Long organisationId, String alertCallReferenceId) {
    return findActiveAlertCall(Instant.now(), organisationId, alertCallReferenceId);
  }

  public Optional<AlertCall> findActiveAlertCall(Instant nowUtc, Long organisationId, String alertCallReferenceId) {
    return findActive(
        AlertCall.class,
        organisationId,
        alertCallReferenceId,
        alertCallActiveTimeFrameInMs,
        nowUtc,
        (o, r) -> alertCallRepository.findByOrganisationIdAndReferenceId(o, r),
        (a) -> a.getUtcDateTime(),
        (a) -> a.getId());
  }

  public Optional<Alert> findActiveAlert(Long organisationId, String alertReferenceId) {
    return findActiveAlert(Instant.now(), organisationId, alertReferenceId);
  }

  public Optional<Alert> findActiveAlert(Instant nowUtc, Long organisationId, String alertReferenceId) {
    return findActive(
        Alert.class,
        organisationId,
        alertReferenceId,
        alertActiveTimeFrameInMs,
        nowUtc,
        (o, r) -> alertRepository.findByOrganisationIdAndReferenceId(o, r),
        (a) -> a.getUtcDateTime(),
        (a) -> a.getId());
  }

  private <T> Optional<T> findActive(
      Class<T> clazz,
      Long organisationId,
      String referenceId,
      long activeTimeFrameInMs,
      Instant nowUtc,
      BiFunction<Long, String, List<T>> executeLookup,
      Function<T, Date> extractUtcDate,
      Function<T, Long> extractId) {

    String name = clazz.getSimpleName();
    List<T> foundObjects = executeLookup.apply(organisationId, referenceId);

    List<T> foundObjectsSortedByUtcDate = foundObjects
        .stream()
        .sorted(Comparator.<T>comparingLong(a -> extractUtcDate.apply(a).getTime()).reversed())
        .collect(Collectors.toList());

    logger.info("Found all {}s in "
            + "organisationId '{}' "
            + "with referenceId '{}'. "
            + "Found {} {}s",
        name, organisationId, referenceId, foundObjectsSortedByUtcDate.size(), name);

    if (foundObjectsSortedByUtcDate.isEmpty()) {
      return Optional.empty();
    }

    T lastObject = foundObjectsSortedByUtcDate.get(0);
    logger.info("The most recent {} in "
            + "organisationId '{}' "
            + "with referenceId '{}' "
            + "is {} with Id: {}",
        name, organisationId, referenceId, name, extractId.apply(lastObject));

    Instant objectCreatedUtc = extractUtcDate.apply(lastObject).toInstant();
    Instant objectActiveUntilUtc = objectCreatedUtc.plusMillis(activeTimeFrameInMs);

    boolean objectDateExceedsTimeFrame = nowUtc.isAfter(objectActiveUntilUtc);

    // If the time frame is exceeded we can assume that it is a new Alert
    // and not another call for an existing Alert!
    if (objectDateExceedsTimeFrame) {
      logger.info("{} Id {} was found for ReferenceId '{}' in organisationId '{}'. "
              + "But this {} was created at {}. That is outside of the time frame ({}ms) "
              + "to treat the existing {} as active and reuse the existing {}"
              + "for further activity. {} was active till {}",
          name,
          extractId.apply(lastObject),
          referenceId,
          organisationId,
          name,
          DateTimeHelper.toISOString(extractUtcDate.apply(lastObject)),
          alertActiveTimeFrameInMs,
          name,
          name,
          name,
          DateTimeHelper.toISOString(objectActiveUntilUtc));
      return Optional.empty();
    }

    logger.info("{} Id {} was found for ReferenceId '{}' in organisationId '{}'. "
            + "This {} was created at {}. That is inside of the time frame ({}ms) "
            + "to treat the existing {} as active and reuse the existing {}"
            + "for further activity. {} remains active until {}",
        name,
        extractId.apply(lastObject),
        referenceId,
        organisationId,
        name,
        DateTimeHelper.toISOString(extractUtcDate.apply(lastObject)),
        alertActiveTimeFrameInMs,
        name,
        name,
        name,
        DateTimeHelper.toISOString(objectActiveUntilUtc));
    return Optional.of(lastObject);
  }

  private ExistingAlert createAlertIfNotExists(Long organisationId,
      String referenceId,
      String keyword,
      Date utcDateTime,
      String address,
      String description) {

    logger.info("Start waiting before looking for existing alert");
    StopWatch stopWatch = StopWatch.createStarted();
    synchronized (this) {
      logger.info("Finished waiting before looking for existing alert (Took: {})", stopWatch.toString());

      Instant utcInstant = Instant.ofEpochMilli(utcDateTime.getTime());
      Optional<Alert> foundActiveAlert = findActiveAlert(utcInstant, organisationId, referenceId);

      if (foundActiveAlert.isEmpty()) {
        logger.info(
            "No Alert with ReferenceId '{}' in organisationId '{}' in the specific alert-active-time-frame found",
            referenceId, organisationId);

        Alert alert = new Alert(organisationId,
            referenceId,
            true,
            keyword,
            utcDateTime,
            description,
            address,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null);
        alertRepository.save(alert);
        return new ExistingAlert(alert, true);
      }
      logger.info(
          "Alert '{}' already exists with ReferenceId '{}'" + " in organisationId '{}'",
          foundActiveAlert.get().getId(),
          referenceId,
          organisationId);
      return new ExistingAlert(foundActiveAlert.get(), false);
    }
  }

  private void processAlert(Alert alert) {
    String addressInfo1 = null;
    String addressInfo2 = null;
    String addressLat = null;
    String addressLng = null;
    String addressJson = null;

    String routeJson = null;
    Double routeDistance = null;
    Integer routeDuration = null;

    if (!StringUtils.isBlank(alert.getAddress())) {
      try {
        GeocodingResult geocodedAddress = geocodingService.geocode(alert.getAddress());
        addressInfo1 = geocodedAddress.getAddressInfo1();
        addressInfo2 = geocodedAddress.getAddressInfo2();
        addressLat = geocodedAddress.getCoordinate().getLat();
        addressLng = geocodedAddress.getCoordinate().getLng();
        addressJson = geocodedAddress.getJson();

        Coordinate orgCoordinate = getOrgCoordinate(alert.getOrganisationId());
        if (orgCoordinate != null) {
          Coordinate targetCoordinate = geocodedAddress.getCoordinate();
          RoutingResult route = routingService.route(new ArrayList<>(Arrays.asList(orgCoordinate, targetCoordinate)));
          routeJson = route.getJson();
          routeDistance = route.getDistance();
          routeDuration = route.getDuration();
        } else {
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
      aaoMatchResult = ruleService.evaluateAao(alert.getOrganisationId(),
          new AlertContext(alert.getKeyword(), alert.getUtcDateTime(), addressInfo2));
    } catch (Exception e) {
      logger.error("Error during aao evaluation", e);
    }

    alert.setAddressInfo1(addressInfo1);
    alert.setAddressInfo2(addressInfo2);
    alert.setAddressLat(addressLat);
    alert.setAddressLng(addressLng);
    alert.setAddressGeocoded(addressJson);
    alert.setRoute(routeJson);
    alert.setDistance(routeDistance);
    alert.setDuration(routeDuration);
    alert.setAao(new StringList(aaoMatchResult.getResults()));

    alertRepository.save(alert);
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
  public void delete(Long id) {
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

  private static class ExistingAlert {

    private Alert alert;
    private boolean isCreated;

    public ExistingAlert(Alert alert, boolean isCreated) {
      this.alert = alert;
      this.isCreated = isCreated;
    }

    public Alert getAlert() {
      return alert;
    }

    public boolean isCreated() {
      return isCreated;
    }
  }
}
