package com.alarmcontrol.server.notifications.usecases.alertcreated;

import com.alarmcontrol.server.data.EmployeeFeedbackService;
import com.alarmcontrol.server.data.graphql.employeeFeedback.EmployeeFeedback;
import com.alarmcontrol.server.data.models.Alert;
import com.alarmcontrol.server.data.models.EmployeeSkill;
import com.alarmcontrol.server.data.models.Feedback;
import com.alarmcontrol.server.data.models.Organisation;
import com.alarmcontrol.server.data.models.Skill;
import com.alarmcontrol.server.data.repositories.EmployeeSkillRepository;
import com.alarmcontrol.server.data.repositories.OrganisationRepository;
import com.alarmcontrol.server.data.repositories.SkillRepository;
import com.alarmcontrol.server.notifications.core.messaging.Severity;
import com.alarmcontrol.server.scheduling.DelayTaskScheduler;
import com.alarmcontrol.server.notifications.core.NotificationBuilderBase;
import com.alarmcontrol.server.notifications.core.messaging.Message;
import com.alarmcontrol.server.notifications.core.messaging.MessageService;
import com.alarmcontrol.server.notifications.core.config.Contact;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AlertCreatedNotificationBuilder extends
    NotificationBuilderBase<AlertCreatedEvent, AlertCreatedNotificationConfig> {

  private Logger logger = LoggerFactory
      .getLogger(AlertCreatedNotificationBuilder.class);

  private DelayTaskScheduler delayTaskScheduler;
  private MessageService messageService;
  private EmployeeFeedbackService employeeFeedbackService;
  private SkillRepository skillRepository;
  private EmployeeSkillRepository employeeSkillRepository;
  private OrganisationRepository organisationRepository;

  public AlertCreatedNotificationBuilder(
      DelayTaskScheduler delayTaskScheduler, MessageService messageService,
      EmployeeFeedbackService employeeFeedbackService,
      SkillRepository skillRepository,
      EmployeeSkillRepository employeeSkillRepository,
      OrganisationRepository organisationRepository) {
    super(AlertCreatedNotificationConfig.class);
    this.delayTaskScheduler = delayTaskScheduler;
    this.messageService = messageService;
    this.employeeFeedbackService = employeeFeedbackService;
    this.skillRepository = skillRepository;
    this.employeeSkillRepository = employeeSkillRepository;
    this.organisationRepository = organisationRepository;
  }

  @Override
  protected void sendNotificationsInternal(AlertCreatedEvent event,
      AlertCreatedNotificationConfig config,
      List<Contact> contacts) {

    Optional<Organisation> foundOrganisation = organisationRepository.findById(event.getAlert().getOrganisationId());
    scheduleCreatedNotification(foundOrganisation.get(), event.getAlert(), contacts);
    scheduleDelayedUpdateNotifications(foundOrganisation.get(), event.getAlert(), config, contacts);
  }

  private void scheduleCreatedNotification(Organisation organisation, Alert alert,
      List<Contact> contacts) {
    logger.info("New AlertCreatedNotification registered on Scheduler: Immediately");
    delayTaskScheduler.registerOnceScheduledTask(
        () -> notifyAlertCreated(organisation, alert, contacts), 0);
  }

  private void scheduleDelayedUpdateNotifications(Organisation organisation,
      Alert alert,
      AlertCreatedNotificationConfig config,
      List<Contact> contacts) {
    for (Integer notificationDelayInSeconds : config.getUpdateDelaysInSeconds()) {
      if (notificationDelayInSeconds == null) {
        continue;
      }

      delayTaskScheduler.registerOnceScheduledTask(
          () -> alertStatusUpdate(organisation, alert, contacts), notificationDelayInSeconds);
    }
  }

  private void notifyAlertCreated(Organisation organisation, Alert alert,
      List<Contact> contacts) {
    logger.info("Build AlertCreatedNotification");
    Message message = buildAlertCreatedMessage(organisation, alert);
    messageService.send(message, contacts);
  }

  private Message buildAlertCreatedMessage(Organisation organisation, Alert alert) {
    String subject = "Alarm: " + alert.getKeyword();

    String address = "";
    if (!StringUtils.isBlank(alert.getAddressInfo2())) {
      address = alert.getAddressInfo2();
    }

    String description = StringUtils.isBlank(alert.getDescription()) ? "" : alert.getDescription() + "\n";

    String body = alert.getKeyword() + "\n" +
        description +
        "Ortsteil: " + address + "\n\n" +
        "Organisation: " + organisation.getName() + "\n";

    return new Message(Severity.ALERT, subject, body);
  }

  private void alertStatusUpdate(Organisation organisation, Alert alert,
      List<Contact> contacts) {
    logger.info("Build AlertFeedback Notification");
    Message message = buildAlertStatusUpdateMessage(organisation, alert);
    messageService.send(message, contacts);
  }

  private Message buildAlertStatusUpdateMessage(Organisation organisation, Alert alert) {
    List<EmployeeFeedback> feedback = employeeFeedbackService.findByAlertId(alert.getId());

    List<Long> employeeIdsThatCommitted = feedback
        .stream()
        .filter(f -> f.getFeedback() == Feedback.COMMIT)
        .map(f -> f.getEmployeeId())
        .collect(Collectors.toList());

    List<Long> employeeIdsThatCancel = feedback
        .stream()
        .filter(f -> f.getFeedback() == Feedback.CANCEL)
        .map(f -> f.getEmployeeId())
        .collect(Collectors.toList());

    Map<String, Integer> skillCount = groupByEmployeeSkillsAndCount(alert.getOrganisationId(),
        employeeIdsThatCommitted);

    StringBuilder skillBodyPart = new StringBuilder();
    if (skillCount.size() > 0) {
      skillBodyPart.append("\n\nKOMMEN pro Skill:");
      for (Entry<String, Integer> skillCountEntry : skillCount.entrySet()) {
        skillBodyPart.append("\n");
        skillBodyPart.append("* ");
        skillBodyPart.append(skillCountEntry.getKey());
        skillBodyPart.append(": ");
        skillBodyPart.append(skillCountEntry.getValue());
      }
    }

    String subject = "Alarmupdate: " + alert.getKeyword();

    String body = "KOMMEN:" + employeeIdsThatCommitted.size() + "\n"
        + "ABGELEHNT:" + employeeIdsThatCancel.size() + "\n"
        + skillBodyPart.toString() + "\n\n" +
        "Organisation: " +organisation.getName();

    return new Message(Severity.INFO, subject, body);
  }

  private Map<String, Integer> groupByEmployeeSkillsAndCount(Long
      organisationId, List<Long> employeeIdsThatCommitted) {
    Iterable<EmployeeSkill> employeeSkillsThatCommitted = employeeSkillRepository
        .findByEmployeeIdIn(employeeIdsThatCommitted);

    Map<Long, Skill> skills = skillRepository
        .findByOrganisationId(organisationId)
        .stream()
        .collect(Collectors.toMap(s -> s.getId(), s -> s));

    Map<String, Integer> skillCount = new HashMap<>();
    for (EmployeeSkill employeeSkill : employeeSkillsThatCommitted) {
      Skill skill = skills.get(employeeSkill.getSkillId());
      String key = skill.getShortName();
      if (skill != null) {
        Integer count = skillCount.get(key);
        if (count == null) {
          count = 0;
        }
        count++;
        skillCount.put(key, count);
      }
    }
    return skillCount;
  }
}

