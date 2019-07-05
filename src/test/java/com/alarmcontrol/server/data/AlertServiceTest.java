package com.alarmcontrol.server.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.alarmcontrol.server.data.models.Alert;
import com.alarmcontrol.server.data.models.AlertCall;
import com.alarmcontrol.server.data.repositories.AlertRepository;
import com.alarmcontrol.server.data.utils.GraphQLClient;
import com.alarmcontrol.server.data.utils.TestOrganisation;
import com.alarmcontrol.server.maps.Coordinate;
import com.alarmcontrol.server.maps.GeocodingResult;
import com.alarmcontrol.server.maps.GeocodingService;
import com.alarmcontrol.server.maps.RoutingResult;
import com.alarmcontrol.server.maps.RoutingService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfiguration.class})
public class AlertServiceTest {

  public static final String TARGET_ADDRESS_LAT = "50.54321";
  public static final String TARGET_ADDRESS_LNG = "10.12345";

  @MockBean(name = "geocodingService")
  GeocodingService geocodingService;

  @MockBean(name = "routingService")
  RoutingService routingService;

  @Autowired
  private AlertService alertService;

  @Autowired
  private GraphQLClient graphQlClient;

  @Autowired
  private AlertRepository alertRepository;

  @BeforeEach
  public void clear() {
    // reset smth.
  }

  @Test
  public void whenTwoAlertCallsWithSameAlertReferenceId_ShouldCreateOneAlertWithTwoCalls() {
    TestOrganisation organisation = setupOrganisation();

    AlertCall firstAlertCall = alertService.create(
        organisation.getId(),
        "1234-S04",
        "B12345",
        "XY123",
        "H1",
        null,
        "",
        "",
        "");

    AlertCall secondAlertCall = alertService.create(
        organisation.getId(),
        "1234-S04",
        "B12345",
        "XY124",
        "H1",
        null,
        "",
        "",
        "");

    assertThat(firstAlertCall.getAlertId()).isEqualTo(secondAlertCall.getAlertId());
    assertThat(alertRepository.findByOrganisationId(organisation.getId()).size()).isEqualTo(1);
  }

  @Test
  public void whenTwoAlertCallsWithDifferentAlertReferenceId_ShouldCreateTwoAlerts() {
    TestOrganisation organisation = setupOrganisation();

    AlertCall firstAlertCall = alertService.create(
        organisation.getId(),
        "1234-S04",
        "B12345",
        "XY123",
        "H1",
        null,
        "",
        "",
        "");

    AlertCall secondAlertCall = alertService.create(
        organisation.getId(),
        "1234-S04",
        "B54321",
        "XY124",
        "H1",
        null,
        "",
        "",
        "");

    assertThat(firstAlertCall.getAlertId()).isNotEqualTo(secondAlertCall.getAlertId());
    assertThat(alertRepository.findByOrganisationId(organisation.getId()).size()).isEqualTo(2);
  }

  @Test
  public void whenAddressSpecified_UseGeocodingAndRoutingService() {
    TestOrganisation organisation = setupOrganisation();

    when(geocodingService.geocode(anyString())).thenReturn(new GeocodingResult("{}",
        TARGET_ADDRESS_LAT,
        TARGET_ADDRESS_LNG,
        "Musterweg 5",
        "Oberhausen - Demohausen"));

    when(routingService.route(anyList())).thenAnswer(invocationOnMock -> {
          List<Coordinate> coords = invocationOnMock.getArgument(0);
          assertThat(TestOrganisation.ORG_ADDRESS_LAT).isEqualTo(coords.get(0).getLat());
          assertThat(TestOrganisation.ORG_ADDRESS_LNG).isEqualTo(coords.get(0).getLng());

          assertThat(TARGET_ADDRESS_LAT).isEqualTo(coords.get(1).getLat());
          assertThat(TARGET_ADDRESS_LNG).isEqualTo(coords.get(1).getLng());

          return new RoutingResult("{\"route\":\"notrelevant\"}",
              2.5,
              10);
        }
    );

    alertService.create(
        organisation.getId(),
        "1234-S04",
        "B12345",
        "XY123",
        "H1",
        null,
        "Musterweg 5, Demohausen",
        "",
        "");

    Optional<Alert> foundAlert = alertRepository.findByOrganisationIdAndReferenceId(organisation.getId(), "B12345");

    assertThat(foundAlert.get().getAddressInfo1()).isEqualTo("Musterweg 5");
    assertThat(foundAlert.get().getAddressInfo2()).isEqualTo("Oberhausen - Demohausen");
    assertThat(foundAlert.get().getRoute()).isEqualTo("{\"route\":\"notrelevant\"}");
    assertThat(foundAlert.get().getAddressLat()).isEqualTo(TARGET_ADDRESS_LAT);
    assertThat(foundAlert.get().getAddressLng()).isEqualTo(TARGET_ADDRESS_LNG);
  }

  @Test
  public void whenAlertReferenceIdIsEmpty_ThrowsException() {
    TestOrganisation organisation = setupOrganisation();

    Assertions.assertThrows(IllegalArgumentException.class, () ->
        alertService.create(
            organisation.getId(),
            "1234-S04",
            "",
            "XY123",
            "H1",
            null,
            "",
            "",
            "")
    );
  }

  @Test
  public void whenAlertCallReferenceIdIsEmpty_ThrowsException() {
    TestOrganisation organisation = setupOrganisation();

    Assertions.assertThrows(IllegalArgumentException.class, () ->
        alertService.create(
            organisation.getId(),
            "1234-S04",
            "B12345",
            "",
            "H1",
            null,
            "",
            "",
            "")
    );
  }

  private TestOrganisation setupOrganisation() {
    TestOrganisation org = graphQlClient.createOrganisation("Organisation" + UUID.randomUUID());
    org.addAlertNumber("1234-S04", "Pager");
    return org;
  }
}