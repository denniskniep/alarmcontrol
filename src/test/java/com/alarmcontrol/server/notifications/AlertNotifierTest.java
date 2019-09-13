package com.alarmcontrol.server.notifications;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

import com.alarmcontrol.server.AlertBaseTest;
import com.alarmcontrol.server.data.models.Alert;
import com.alarmcontrol.server.data.models.AlertCall;
import com.alarmcontrol.server.data.models.Feedback;
import com.alarmcontrol.server.data.utils.TestOrganisation;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

public class AlertNotifierTest extends AlertBaseTest {

  @MockBean(name = "notificationService")
  private NotificationService notificationService;

  @Autowired
  private AlertNotifier alertNotifier;

  @Test
  public void whenAlertCreated_shouldScheduleNotification(){
    TestOrganisation organisation = setupOrganisation();

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

    Optional<Alert> foundAlert = alertRepository.findById(alertCall.getAlertId());
    List<Message> messages = notify(foundAlert);

    assertThat(messages).extracting(m -> m.getType()).containsExactly(
        AlertNotifier.ALERT_CREATED,
        AlertNotifier.ALERT_UPDATE,
        AlertNotifier.ALERT_UPDATE);
  }

  @Test
  public void whenAlertWithoutFeedback_shouldBuildMessages(){
    TestOrganisation organisation = setupOrganisationWithEmployees();

    AlertCall alertCall = alertService.create(
        organisation.getId(),
        "1234-S04",
        "B12345",
        "XY123",
        "H1",
        null,
        "Mondstraße 15, 12345 Berlin",
        "",
        "");

    Optional<Alert> foundAlert = alertRepository.findById(alertCall.getAlertId());

    Message messageCreated = alertNotifier.buildAlertCreatedMessage(foundAlert.get());
    assertThat(messageCreated.getBody()).isEqualTo("H1\n"
        + "Mondstraße 15, 12345 Berlin");

    Message messageStatusUpdate = alertNotifier.buildAlertStatusUpdateMessage(foundAlert.get());
    assertThat(messageStatusUpdate.getBody()).isEqualTo("KOMMEN:0\n"
        + "ABGELEHNT:0");
  }

  @Test
  public void whenAlertWithOneFeedback_shouldBuildMessages(){
    TestOrganisation organisation = setupOrganisationWithEmployees();

    AlertCall alertCall = alertService.create(
        organisation.getId(),
        "1234-S04",
        "B12345",
        "XY123",
        "H1",
        null,
        "Mondstraße 15, 12345 Berlin",
        "",
        "");

    Optional<Alert> foundAlert = alertRepository.findById(alertCall.getAlertId());

    organisation.addEmployeeFeedback(alertCall.getReferenceId(), EMPLOYEE_HANS, Feedback.COMMIT);

    Message messageCreated = alertNotifier.buildAlertCreatedMessage(foundAlert.get());
    assertThat(messageCreated.getBody()).isEqualTo("H1\n"
        + "Mondstraße 15, 12345 Berlin");

    Message messageStatusUpdate = alertNotifier.buildAlertStatusUpdateMessage(foundAlert.get());
    assertThat(messageStatusUpdate.getBody()).isEqualTo("KOMMEN:1\n"
        + "ABGELEHNT:0\n"
        + "\n"
        + "KOMMEN pro Skill:\n"
        + "* FK: 1\n"
        + "* AGT: 1");
  }

  @Test
  public void whenAlertWithMultiFeedback_shouldBuildMessages(){
    TestOrganisation organisation = setupOrganisationWithEmployees();

    AlertCall alertCall = alertService.create(
        organisation.getId(),
        "1234-S04",
        "B12345",
        "XY123",
        "H1",
        null,
        "Mondstraße 15, 12345 Berlin",
        "",
        "");

    Optional<Alert> foundAlert = alertRepository.findById(alertCall.getAlertId());

    organisation.addEmployeeFeedback(alertCall.getReferenceId(), EMPLOYEE_HANS, Feedback.COMMIT);
    organisation.addEmployeeFeedback(alertCall.getReferenceId(), EMPLOYEE_EDUARD, Feedback.COMMIT);
    organisation.addEmployeeFeedback(alertCall.getReferenceId(), EMPLOYEE_MARINA, Feedback.CANCEL);
    organisation.addEmployeeFeedback(alertCall.getReferenceId(), EMPLOYEE_SABINE, Feedback.COMMIT);

    Message messageCreated = alertNotifier.buildAlertCreatedMessage(foundAlert.get());
    assertThat(messageCreated.getBody()).isEqualTo("H1\n"
        + "Mondstraße 15, 12345 Berlin");

    Message messageStatusUpdate = alertNotifier.buildAlertStatusUpdateMessage(foundAlert.get());
    assertThat(messageStatusUpdate.getBody()).isEqualTo("KOMMEN:3\n"
        + "ABGELEHNT:1\n"
        + "\n"
        + "KOMMEN pro Skill:\n"
        + "* FK: 1\n"
        + "* AGT: 2");
  }

  private List<Message> notify(Optional<Alert> foundAlert) {
    List<ScheduledFuture<?>> futures = alertNotifier.notify(foundAlert.get());
    waitFor(futures);

    ArgumentCaptor<Message> msgArg = ArgumentCaptor.forClass(Message.class);
    verify(notificationService, atLeast(1)).send(msgArg.capture());
    List<Message> messages = msgArg.getAllValues();
    return messages;
  }

  private void waitFor(List<ScheduledFuture<?>> futures) {
    try {
      for (ScheduledFuture<?> future : futures) {
        future.get();
      }
    }catch (InterruptedException | ExecutionException e){
      throw new RuntimeException(e);
    }
  }
}