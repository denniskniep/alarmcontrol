package com.alarmcontrol.server.data;

import static com.alarmcontrol.server.data.graphql.DateTimeHelper.createDate;
import static org.assertj.core.api.Assertions.assertThat;

import com.alarmcontrol.server.data.graphql.employeeFeedback.EmployeeFeedback;
import com.alarmcontrol.server.data.models.AlertCall;
import com.alarmcontrol.server.data.models.AlertCallEmployee;
import com.alarmcontrol.server.data.models.Feedback;
import com.alarmcontrol.server.data.repositories.AlertCallEmployeeRepository;
import com.alarmcontrol.server.data.utils.GraphQLClient;
import com.alarmcontrol.server.data.utils.TestOrganisation;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
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
  private AlertCallEmployeeRepository alertCallEmployeeRepository;

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

  @Test
  public void whenTwoAlertCallsWithSameReferenceIdOnSameAlert_AllEmployeeFeedbackOnSAmeAlert() {
    TestOrganisation organisation = setupOrganisation();
    Long max = organisation.addEmployee("Max", "Mustermann", "1234-5678");
    Long petra = organisation.addEmployee("Petra", "Demofrau", "1234-6789");
    Long jorge = organisation.addEmployee("Jorge", "Demomann", "1234-1234");

    AlertCall alertCallA = alertService.create(
        organisation.getId(),
        "1234-S04",
        "B12345",
        "123",
        "H1",
        createDate(2020, 03, 28, 15, 00, 00),
        "",
        "",
        "");

    employeeFeedbackService.addEmployeeFeedback(
        organisation.getId(),
        "123",
        "1234-5678",
        Feedback.COMMIT,
        createDate(2020, 03, 28, 15, 01, 00),
        null);

    employeeFeedbackService.addEmployeeFeedback(
        organisation.getId(),
        "123",
        "1234-1234",
        Feedback.COMMIT,
        createDate(2020, 03, 28, 15, 02, 00),
        null);

    AlertCall alertCallB = alertService.create(
        organisation.getId(),
        "1234-S04",
        "B12345",
        "123",
        "H1",
        createDate(2020, 03, 28, 15, 05, 40),
        "",
        "",
        "");

    employeeFeedbackService.addEmployeeFeedback(
        organisation.getId(),
        "123",
        "1234-6789",
        Feedback.COMMIT,
        createDate(2020, 03, 28, 15, 06, 50),
        null);

    assertThat(alertCallA.getAlertId()).isEqualTo(alertCallB.getAlertId());

    List<EmployeeFeedback> employeeFeedbacks = employeeFeedbackService.findByAlertId(alertCallA.getAlertId());
    assertThat(employeeFeedbacks).hasSize(3);
    assertThat(employeeFeedbacks).allMatch(ef -> ef.getFeedback().equals(Feedback.COMMIT));

    List<AlertCallEmployee> byAlertId = alertCallEmployeeRepository.findByAlertId(alertCallA.getAlertId());
    assertThat(byAlertId.stream().filter(a -> a.getEmployeeId() == max).findFirst().get().getAlertCallId())
        .isEqualTo(alertCallA.getId());
    assertThat(byAlertId.stream().filter(a -> a.getEmployeeId() == jorge).findFirst().get().getAlertCallId())
        .isEqualTo(alertCallA.getId());

    assertThat(byAlertId.stream().filter(a -> a.getEmployeeId() == petra).findFirst().get().getAlertCallId())
        .isEqualTo(alertCallB.getId());
  }


  @Test
  public void whenAlertCallsIsNotActiveAnymore_ThrowsException() {
    TestOrganisation organisation = setupOrganisation();
    Long max = organisation.addEmployee("Max", "Mustermann", "1234-5678");

    AlertCall alertCallA = alertService.create(
        organisation.getId(),
        "1234-S04",
        "B12345",
        "123",
        "H1",
        createDate(2020, 03, 28, 15, 00, 00),
        "",
        "",
        "");

    Assertions.assertThrows(IllegalArgumentException.class, () ->
        employeeFeedbackService.addEmployeeFeedback(
            organisation.getId(),
            "123",
            "1234-5678",
            Feedback.COMMIT,
            createDate(2020, 04, 28, 15, 01, 00),
            null)
    );
}

  private boolean isEmployeeFeedback(EmployeeFeedback f, Long employeeId, Feedback feedback) {
    return f.getEmployeeId().equals(employeeId) && feedback.equals(f.getFeedback());
  }

  private TestOrganisation setupOrganisation() {
    TestOrganisation org = graphQlClient.createOrganisation("Organisation" + UUID.randomUUID());
    org.addAlertNumber("1234-S04", "Pager");
    return org;
  }
}