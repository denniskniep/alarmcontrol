package com.alarmcontrol.server.data;

import static org.assertj.core.api.Assertions.assertThat;

import com.alarmcontrol.server.data.graphql.employeeFeedback.EmployeeFeedback;
import com.alarmcontrol.server.data.models.AlertCall;
import com.alarmcontrol.server.data.models.Feedback;
import com.alarmcontrol.server.data.utils.GraphQLClient;
import com.alarmcontrol.server.data.utils.TestOrganisation;
import java.util.List;
import java.util.UUID;
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
public class EmployeeFeedbackServiceTest {

  @Autowired
  private AlertService alertService;

  @Autowired
  private EmployeeFeedbackService employeeFeedbackService;

  @Autowired
  private GraphQLClient graphQlClient;

  @Test
  public void whenNoEmployeeFeedbackSent_shouldReturnNoResponse() {
    TestOrganisation organisation = setupOrganisation();
    Long firstEmployeeId = organisation.addEmployee("Max", "Mustermann", "1234-5678");
    Long secondEmployeeId = organisation.addEmployee("Petra", "Demofrau", "1234-6789");

    AlertCall alertCall = alertService.create(
        organisation.getId(),
        "1234-S04",
        "B12345",
        "XY123",
        "H1",
        null,
        "",
        "",
        "");

    List<EmployeeFeedback> employeeFeedbacks = employeeFeedbackService.findByAlertId(alertCall.getAlertId());
    assertThat(employeeFeedbacks).anyMatch(f -> isEmployeeFeedback(f, firstEmployeeId, Feedback.NO_RESPONSE));
    assertThat(employeeFeedbacks).anyMatch(f -> isEmployeeFeedback(f, secondEmployeeId, Feedback.NO_RESPONSE));
  }

  @Test
  public void whenOneCommitSent_shouldReturnCommitAndOtherWithNoResponse() {
    TestOrganisation organisation = setupOrganisation();
    Long firstEmployeeId = organisation.addEmployee("Max", "Mustermann", "1234-5678");
    Long secondEmployeeId = organisation.addEmployee("Petra", "Demofrau", "1234-6789");

    AlertCall alertCall = alertService.create(
        organisation.getId(),
        "1234-S04",
        "B12345",
        "XY123",
        "H1",
        null,
        "",
        "",
        "");

    employeeFeedbackService.addEmployeeFeedback(
        organisation.getId(),
        "XY123",
        "1234-6789",
        Feedback.COMMIT,
        null,
        null);

    List<EmployeeFeedback> employeeFeedbacks = employeeFeedbackService.findByAlertId(alertCall.getAlertId());
    assertThat(employeeFeedbacks).anyMatch(f -> isEmployeeFeedback(f, firstEmployeeId, Feedback.NO_RESPONSE));
    assertThat(employeeFeedbacks).anyMatch(f -> isEmployeeFeedback(f, secondEmployeeId, Feedback.COMMIT));
  }

  @Test
  public void whenBothEmployeeFeedbackSent_shouldReturnCommitAndCancel() {
    TestOrganisation organisation = setupOrganisation();
    Long firstEmployeeId = organisation.addEmployee("Max", "Mustermann", "1234-5678");
    Long secondEmployeeId = organisation.addEmployee("Petra", "Demofrau", "1234-6789");

    AlertCall alertCall = alertService.create(
        organisation.getId(),
        "1234-S04",
        "B12345",
        "XY123",
        "H1",
        null,
        "",
        "",
        "");

    employeeFeedbackService.addEmployeeFeedback(
        organisation.getId(),
        "XY123",
        "1234-6789",
        Feedback.COMMIT,
        null,
        null);

    employeeFeedbackService.addEmployeeFeedback(
        organisation.getId(),
        "XY123",
        "1234-5678",
        Feedback.CANCEL,
        null,
        null);

    List<EmployeeFeedback> employeeFeedbacks = employeeFeedbackService.findByAlertId(alertCall.getAlertId());
    assertThat(employeeFeedbacks).anyMatch(f -> isEmployeeFeedback(f, firstEmployeeId, Feedback.CANCEL));
    assertThat(employeeFeedbacks).anyMatch(f -> isEmployeeFeedback(f, secondEmployeeId, Feedback.COMMIT));
  }

  @Test
  public void whenOneEmployeeSendMultipleFeedbacks_shouldReturnTheLatest() {
    TestOrganisation organisation = setupOrganisation();
    Long firstEmployeeId = organisation.addEmployee("Max", "Mustermann", "1234-5678");
    Long secondEmployeeId = organisation.addEmployee("Petra", "Demofrau", "1234-6789");

    AlertCall alertCall = alertService.create(
        organisation.getId(),
        "1234-S04",
        "B12345",
        "XY123",
        "H1",
        null,
        "",
        "",
        "");

    employeeFeedbackService.addEmployeeFeedback(
        organisation.getId(),
        "XY123",
        "1234-6789",
        Feedback.COMMIT,
        null,
        null);

    List<EmployeeFeedback> employeeFeedbacks = employeeFeedbackService.findByAlertId(alertCall.getAlertId());
    assertThat(employeeFeedbacks).anyMatch(f -> isEmployeeFeedback(f, secondEmployeeId, Feedback.COMMIT));

    employeeFeedbackService.addEmployeeFeedback(
        organisation.getId(),
        "XY123",
        "1234-6789",
        Feedback.CANCEL,
        null,
        null);

    employeeFeedbacks = employeeFeedbackService.findByAlertId(alertCall.getAlertId());
    assertThat(employeeFeedbacks).anyMatch(f -> isEmployeeFeedback(f, firstEmployeeId, Feedback.NO_RESPONSE));
    assertThat(employeeFeedbacks).anyMatch(f -> isEmployeeFeedback(f, secondEmployeeId, Feedback.CANCEL));

    assertThat(employeeFeedbacks)
        .filteredOn(f -> f.getEmployeeId().equals(secondEmployeeId))
        .hasSize(1);
  }

  private boolean isEmployeeFeedback( EmployeeFeedback f, Long employeeId, Feedback feedback) {
    return f.getEmployeeId().equals(employeeId) && feedback.equals(f.getFeedback());
  }

  private TestOrganisation setupOrganisation() {
    TestOrganisation org = graphQlClient.createOrganisation("Organisation" + UUID.randomUUID());
    org.addAlertNumber("1234-S04", "Pager");
    return org;
  }
}