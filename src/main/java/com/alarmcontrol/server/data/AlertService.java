package com.alarmcontrol.server.data;

import com.alarmcontrol.server.data.graphql.alert.publisher.AlertAddedPublisher;
import com.alarmcontrol.server.data.repositories.AlertRepository;
import com.alarmcontrol.server.data.repositories.OrganisationRepository;
import com.alarmcontrol.server.maps.graphhopper.routing.GraphhopperRoutingService;
import com.alarmcontrol.server.maps.mapbox.geocoding.MapboxGeocodingService;
import org.springframework.stereotype.Service;

@Service
public class AlertService {

  private AlertRepository alertRepository;
  private MapboxGeocodingService geocodingService;
  private GraphhopperRoutingService routingService;
  private OrganisationRepository organisationRepository;
  private AlertAddedPublisher alertAddedPublisher;

  public AlertService(AlertRepository alertRepository,
      MapboxGeocodingService geocodingService,
      GraphhopperRoutingService routingService,
      OrganisationRepository organisationRepository,
      AlertAddedPublisher alertAddedPublisher) {
    this.alertRepository = alertRepository;
    this.geocodingService = geocodingService;
    this.routingService = routingService;
    this.organisationRepository = organisationRepository;
    this.alertAddedPublisher = alertAddedPublisher;
  }
/*
  public Alert create(Long organisationId,
      String keyword,
      Date dateTime,
      String description,
      String address){

    if(dateTime == null){
      dateTime = new Date();
    }

    GeocodingResult geocodedAddress = geocodingService.geocode(address);
    Coordinate orgCoordinate = getOrgCoordinate(organisationId);
    Coordinate targetCoordinate = geocodedAddress.getCoordinate();
    RoutingResult route = routingService.route(new ArrayList<>(Arrays.asList(orgCoordinate, targetCoordinate)));

    Alert alert = new Alert(organisationId,
        null,
        true,
        keyword,
        dateTime,
        description,
        address,
        geocodedAddress.getAddressInfo1(),
        geocodedAddress.getAddressInfo2(),
        geocodedAddress.getCoordinate().getLat(),
        geocodedAddress.getCoordinate().getLng(),
        geocodedAddress.getJson(),
        route.getJson(),
        route.getDistance(),
        route.getDuration());
    alertRepository.save(alert);
    alertAddedPublisher.emitAlertAdded(alert.getId());
    return alert;
  }

  private Coordinate getOrgCoordinate(Long organisationId) {

    Optional<Organisation> orgById = organisationRepository.findById(organisationId);
    if(!orgById.isPresent()){
      throw new IllegalArgumentException("No organisation found for id '"+orgById+"'");
    }

    Organisation organisation = orgById.get();
    String orgLat = organisation.getAddressLat();
    String orgLng = organisation.getAddressLng();
    return new Coordinate(orgLat, orgLng);
  }*/
}
