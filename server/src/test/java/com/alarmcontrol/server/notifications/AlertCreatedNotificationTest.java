package com.alarmcontrol.server.notifications;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.alarmcontrol.server.AlertBaseTest;
import com.alarmcontrol.server.data.OrganisationConfigurationService;
import com.alarmcontrol.server.data.models.AlertCall;
import com.alarmcontrol.server.data.models.Feedback;
import com.alarmcontrol.server.data.utils.TestOrganisation;
import com.alarmcontrol.server.maps.GeocodingResult;
import com.alarmcontrol.server.maps.GeocodingService;
import com.alarmcontrol.server.notifications.core.messaging.Message;
import com.alarmcontrol.server.notifications.core.messaging.MessageService;
import com.alarmcontrol.server.notifications.core.config.NotificationOrganisationConfiguration;
import com.alarmcontrol.server.notifications.core.config.NotificationSubscription;
import com.alarmcontrol.server.notifications.messaging.mail.MailContact;
import com.alarmcontrol.server.notifications.usecases.alertcreated.AlertCreatedNotificationConfig;
import com.alarmcontrol.server.scheduling.DelayTaskScheduler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class AlertCreatedNotificationTest extends AlertBaseTest {

  @MockBean
  private MessageService messageService;
  @Autowired
  private DelayTaskScheduler delayTaskScheduler;
  @Autowired
  private OrganisationConfigurationService organisationConfigurationService;
  @MockBean(name = "geocodingService")
  private GeocodingService geocodingService;

  @Test
  public void whenAlertCreated_shouldScheduleNotifications() {
    TestOrganisation organisation = setupOrganisation();
    createAlertNotificationConfig(organisation.getId(), 1, 2, 3);

    alertService.create(
        organisation.getId(),
        "1234-S04",
        "B12345",
        "XY123",
        "H1",
        null,
        "",
        "",
        "");

    List<Message> messages = waitForAllMessages();
    assertThat(messages).hasSize(4);
    assertThat(messages).extracting(m -> m.getSubject()).areExactly(1, isStartingWith("Alarm: H1"));
    assertThat(messages).extracting(m -> m.getSubject()).areExactly(3, isStartingWith("Alarmupdate: H1"));
  }

  @Test
  public void whenAlertWithoutFeedback_shouldBuildMessages() {
    TestOrganisation organisation = setupOrganisationWithEmployees();
    createAlertNotificationConfig(organisation.getId(), 0);

    geocodeAnyStringWith("Mondstraße 15", "Berlin-Tempelhof");

    alertService.create(
        organisation.getId(),
        "1234-S04",
        "B12345",
        "XY123",
        "H1",
        null,
        "Mondstraße 15, 12345 Berlin",
        "",
        "");

    List<Message> messages = waitForAllMessages();

    assertThat(messages).hasSize(2);
    assertThat(messages).extracting(m -> m.getBody()).areExactly(1, isStartingWith("H1\n"
        + "Ortsteil: Berlin-Tempelhof"));

    assertThat(messages).extracting(m -> m.getBody()).areExactly(1, isStartingWith("KOMMEN:0\n"
        + "ABGELEHNT:0"));
  }

  @Test
  public void whenAlertWithOneFeedback_shouldBuildMessages() {
    TestOrganisation organisation = setupOrganisationWithEmployees();
    createAlertNotificationConfig(organisation.getId(), 1);

    geocodeAnyStringWith("Mondstraße 15", "Berlin-Tempelhof");

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

    // The Notification task is at this point already scheduled!
    // If there goes something wrong, maybe the notification is already sent
    // before the EmployeeFeedback is stored in the database
    organisation.addEmployeeFeedback(alertCall.getReferenceId(), EMPLOYEE_HANS, Feedback.COMMIT);

    List<Message> messages = waitForAllMessages();

    assertThat(messages).hasSize(2);
    assertThat(messages).extracting(m -> m.getBody()).areExactly(1, isStartingWith("H1\n"
        + "Ortsteil: Berlin-Tempelhof\n"
        + "\n"
        + "Organisation: Organisation"));

    assertThat(messages).extracting(m -> m.getBody()).areExactly(1, isStartingWith("KOMMEN:1\n"
        + "ABGELEHNT:0\n"
        + "\n"
        + "\n"
        + "KOMMEN pro Skill:\n"
        + "* FK: 1\n"
        + "* AGT: 1\n"
        + "\n"
        + "Organisation"));
  }

  @Test
  public void whenAlertWithMultiFeedback_shouldBuildMessages() {
    TestOrganisation organisation = setupOrganisationWithEmployees();

    createAlertNotificationConfig(organisation.getId(), 1);

    geocodeAnyStringWith("Mondstraße 15", "Berlin-Tempelhof");

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

    // The Notification task is at this point already scheduled!
    // If there goes something wrong, maybe the notification is already sent
    // before the EmployeeFeedback is stored in the database
    organisation.addEmployeeFeedback(alertCall.getReferenceId(), EMPLOYEE_HANS, Feedback.COMMIT);
    organisation.addEmployeeFeedback(alertCall.getReferenceId(), EMPLOYEE_EDUARD, Feedback.COMMIT);
    organisation.addEmployeeFeedback(alertCall.getReferenceId(), EMPLOYEE_MARINA, Feedback.CANCEL);
    organisation.addEmployeeFeedback(alertCall.getReferenceId(), EMPLOYEE_SABINE, Feedback.COMMIT);

    List<Message> messages = waitForAllMessages();

    assertThat(messages).hasSize(2);
    assertThat(messages).extracting(m -> m.getBody()).areExactly(1, isStartingWith("H1\n"
        + "Ortsteil: Berlin-Tempelhof\n"
        + "\n"
        + "Organisation: Organisation"));

    assertThat(messages).extracting(m -> m.getBody()).areExactly(1, isStartingWith("KOMMEN:3\n"
        + "ABGELEHNT:1\n"
        + "\n"
        + "\n"
        + "KOMMEN pro Skill:\n"
        + "* FK: 1\n"
        + "* AGT: 2\n"
        + "\n"
        + "Organisation: Organisation"));
  }

  private List<Message> waitForAllMessages() {
    List<ScheduledFuture<?>> pendingTasks = delayTaskScheduler.getPendingTasks();
    waitFor(pendingTasks);

    ArgumentCaptor<Message> msgArg = ArgumentCaptor.forClass(Message.class);
    verify(messageService, atLeast(1)).send(msgArg.capture(), ArgumentMatchers.any());
    List<Message> messages = msgArg.getAllValues();
    return messages;
  }

  private void waitFor(List<ScheduledFuture<?>> futures) {
    try {
      for (ScheduledFuture<?> future : futures) {
        future.get();
      }
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

  private void createAlertNotificationConfig(Long organisationId, Integer... updateDelaysInSeconds) {
    AlertCreatedNotificationConfig config = new AlertCreatedNotificationConfig();
    config.setUpdateDelaysInSeconds(new ArrayList<>(Arrays.asList(updateDelaysInSeconds)));
    createAlertNotificationConfig(organisationId, config);
  }

  private void createAlertNotificationConfig(Long organisationId, AlertCreatedNotificationConfig config) {
    MailContact dummyContact = new MailContact();
    dummyContact.setUniqueId("1234");

    NotificationSubscription subscription = new NotificationSubscription();
    subscription.setNotificationConfig(config);
    subscription.setSubscriberContactUniqueIds(new ArrayList<>(Arrays.asList("1234")));

    NotificationOrganisationConfiguration orgConfig = new NotificationOrganisationConfiguration();
    orgConfig.setContacts(new ArrayList<>(Arrays.asList(dummyContact)));
    orgConfig.setSubscriptions(new ArrayList<>(Arrays.asList(subscription)));

    organisationConfigurationService.saveNotificationConfig(organisationId, orgConfig);
  }

  private Condition<String> isStartingWith(String prefix) {
    return new Condition<>((v) -> v.startsWith(prefix),
        "Expected to start with '" + prefix + "'");
  }

  private Condition<String> isContaining(String value) {
    return new Condition<>((v) -> StringUtils.containsIgnoreCase(v, value),
        "Expected to contain " + value + "'");
  }

  private Condition<String> isEqual(String value) {
    return new Condition<>((v) -> StringUtils.equalsIgnoreCase(v, value),
        "Expected to be equal with " + value + "'");
  }

  private void geocodeAnyStringWith(String addressInfo1, String addressInfo2) {
    when(geocodingService.geocode(anyString())).thenReturn(new GeocodingResult("{}",
        TARGET_ADDRESS_LAT,
        TARGET_ADDRESS_LNG,
        addressInfo1,
        addressInfo2));
  }

  @TestConfiguration
  public static class TestConfig {

    @Bean
    public JavaMailSender javaMailSender() {
      return new JavaMailSenderImpl();
    }
  }
}