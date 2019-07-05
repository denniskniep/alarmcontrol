package com.alarmcontrol.server.data;

import static org.assertj.core.api.Assertions.assertThat;

import com.alarmcontrol.server.data.models.AlertCall;
import com.alarmcontrol.server.data.repositories.AlertRepository;
import com.alarmcontrol.server.data.utils.GraphQLClient;
import com.alarmcontrol.server.data.utils.TestOrganisation;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfiguration.class})
public class AlertServiceTest {

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
  public void whenTwoAlertCallsWithSameAlertReferenceId_ShouldCreateOneAlertWithTwoCalls(){
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
  public void whenTwoAlertCallsWithDifferentAlertReferenceId_ShouldCreateTwoAlerts(){
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

  private TestOrganisation setupOrganisation(){
    TestOrganisation org = graphQlClient.createOrganisation("Organisation" + UUID.randomUUID());
    org.addAlertNumber("1234-S04", "Pager");
    return org;
  }
}