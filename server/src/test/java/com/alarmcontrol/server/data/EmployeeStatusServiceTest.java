package com.alarmcontrol.server.data;

import static com.alarmcontrol.server.data.graphql.DateTimeHelper.toDate;
import static org.assertj.core.api.Assertions.assertThat;

import com.alarmcontrol.server.AlertBaseTest;
import com.alarmcontrol.server.data.models.EmployeeStatus;
import com.alarmcontrol.server.data.models.Status;
import com.alarmcontrol.server.data.utils.TestOrganisation;
import java.time.LocalDateTime;
import java.util.Date;
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
public class EmployeeStatusServiceTest extends AlertBaseTest {

  private static Date MON_15_30 = toDate(LocalDateTime.of(2020, 1, 6, 15, 30, 00));
  private static Date MON_16_00 = toDate(LocalDateTime.of(2020, 1, 6, 16, 00, 00));
  private static Date MON_16_30 = toDate(LocalDateTime.of(2020, 1, 6, 16, 30, 00));
  private static Date WED_16_30 = toDate(LocalDateTime.of(2020, 1, 8, 16, 30, 00));

  @Autowired
  private EmployeeStatusService employeeStatusService;

  @Test
  public void whenNoEmployeeStatusSent_shouldReturnNoResponse() {
    TestOrganisation organisation = setupOrganisationWithEmployees();
    Long idMax = organisation.addEmployee("Max", "Mustermann", "1234");
    EmployeeStatus employeeStatus = employeeStatusService.getEmployeeStatus(idMax);
    assertThat(employeeStatus).isEqualTo(null);
  }

  @Test
  public void whenEmployeeStatusAvailableSent_shouldReturnAvailable() {
    TestOrganisation organisation = setupOrganisationWithEmployees();
    Long idMax = organisation.addEmployee("Max", "Mustermann", "1234");

    employeeStatusService
        .addEmployeeStatus(organisation.getId(), "1234", Status.AVAILABLE, MON_15_30, null);

    EmployeeStatus employeeStatus = employeeStatusService.getEmployeeStatus(idMax);

    assertThat(employeeStatus.getStatus()).isEqualTo(Status.AVAILABLE);
  }

  @Test
  public void whenEmployeeStatusNotAvailableThenAvailableSent_shouldReturnAvailable() {
    TestOrganisation organisation = setupOrganisationWithEmployees();
    Long idMax = organisation.addEmployee("Max", "Mustermann", "1234");

    employeeStatusService
        .addEmployeeStatus(organisation.getId(), "1234", Status.AVAILABLE, MON_16_30, null);

    employeeStatusService
        .addEmployeeStatus(organisation.getId(), "1234", Status.NOT_AVAILABLE, MON_15_30, null);

    EmployeeStatus employeeStatus = employeeStatusService.getEmployeeStatus(idMax);

    assertThat(employeeStatus.getStatus()).isEqualTo(Status.AVAILABLE);
  }

  @Test
  public void whenEmployeeStatusAvailableThenNotAvailableSent_shouldReturnNotAvailable() {
    TestOrganisation organisation = setupOrganisationWithEmployees();
    Long idMax = organisation.addEmployee("Max", "Mustermann", "1234");

    employeeStatusService
        .addEmployeeStatus(organisation.getId(), "1234", Status.NOT_AVAILABLE, MON_16_30, null);

    employeeStatusService
        .addEmployeeStatus(organisation.getId(), "1234", Status.AVAILABLE, MON_15_30, null);

    EmployeeStatus employeeStatus = employeeStatusService.getEmployeeStatus(idMax);

    assertThat(employeeStatus.getStatus()).isEqualTo(Status.NOT_AVAILABLE);
  }

  @Test
  public void whenEmployeeStatusAvailableThenNotAvailableThenAvailableSent_shouldReturnAvailable() {
    TestOrganisation organisation = setupOrganisationWithEmployees();
    Long idMax = organisation.addEmployee("Max", "Mustermann", "1234");

    employeeStatusService
        .addEmployeeStatus(organisation.getId(), "1234", Status.NOT_AVAILABLE, MON_16_30, null);

    employeeStatusService
        .addEmployeeStatus(organisation.getId(), "1234", Status.AVAILABLE, MON_15_30, null);

    employeeStatusService
        .addEmployeeStatus(organisation.getId(), "1234", Status.AVAILABLE, WED_16_30, null);

    EmployeeStatus employeeStatus = employeeStatusService.getEmployeeStatus(idMax);

    assertThat(employeeStatus.getStatus()).isEqualTo(Status.AVAILABLE);
  }

  @Test
  public void untilOrEqual_MON_16_30_shouldReturnNotAvailable() {
    TestOrganisation organisation = setupOrganisationWithEmployees();
    Long idMax = organisation.addEmployee("Max", "Mustermann", "1234");

    employeeStatusService
        .addEmployeeStatus(organisation.getId(), "1234", Status.NOT_AVAILABLE, MON_16_30, null);

    employeeStatusService
        .addEmployeeStatus(organisation.getId(), "1234", Status.AVAILABLE, MON_15_30, null);

    employeeStatusService
        .addEmployeeStatus(organisation.getId(), "1234", Status.AVAILABLE, WED_16_30, null);

    EmployeeStatus employeeStatus = employeeStatusService.getEmployeeStatusUntilOrEqual(idMax, MON_16_30);

    assertThat(employeeStatus.getStatus()).isEqualTo(Status.NOT_AVAILABLE);
  }

  @Test
  public void untilOrEqual_MON_15_30_shouldReturnAvailable() {
    TestOrganisation organisation = setupOrganisationWithEmployees();
    Long idMax = organisation.addEmployee("Max", "Mustermann", "1234");

    employeeStatusService
        .addEmployeeStatus(organisation.getId(), "1234", Status.NOT_AVAILABLE, MON_16_30, null);

    employeeStatusService
        .addEmployeeStatus(organisation.getId(), "1234", Status.AVAILABLE, MON_15_30, null);

    employeeStatusService
        .addEmployeeStatus(organisation.getId(), "1234", Status.AVAILABLE, WED_16_30, null);

    EmployeeStatus employeeStatus = employeeStatusService.getEmployeeStatusUntilOrEqual(idMax, MON_15_30);

    assertThat(employeeStatus.getStatus()).isEqualTo(Status.AVAILABLE);
  }

  @Test
  public void untilOrEqual_MON_16_00_shouldReturnAvailable() {
    TestOrganisation organisation = setupOrganisationWithEmployees();
    Long idMax = organisation.addEmployee("Max", "Mustermann", "1234");

    employeeStatusService
        .addEmployeeStatus(organisation.getId(), "1234", Status.AVAILABLE, MON_15_30, null);

    employeeStatusService
        .addEmployeeStatus(organisation.getId(), "1234", Status.NOT_AVAILABLE, MON_16_30, null);

    employeeStatusService
        .addEmployeeStatus(organisation.getId(), "1234", Status.AVAILABLE, WED_16_30, null);

    EmployeeStatus employeeStatus = employeeStatusService.getEmployeeStatusUntilOrEqual(idMax, MON_16_00);

    assertThat(employeeStatus.getStatus()).isEqualTo(Status.AVAILABLE);
  }
}