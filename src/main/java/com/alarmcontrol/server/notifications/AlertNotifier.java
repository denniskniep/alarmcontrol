package com.alarmcontrol.server.notifications;

import com.alarmcontrol.server.data.EmployeeFeedbackService;
import com.alarmcontrol.server.data.graphql.employeeFeedback.EmployeeFeedback;
import com.alarmcontrol.server.data.models.Alert;
import com.alarmcontrol.server.data.models.EmployeeSkill;
import com.alarmcontrol.server.data.models.Feedback;
import com.alarmcontrol.server.data.models.Skill;
import com.alarmcontrol.server.data.repositories.EmployeeSkillRepository;
import com.alarmcontrol.server.data.repositories.SkillRepository;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

@Service
public class AlertNotifier {

  public final static String ALERT_CREATED = "alert-created";
  public final static String ALERT_UPDATE = "alert-update";
  private Logger logger = LoggerFactory.getLogger(AlertNotifier.class);

  @Value("${alertnotifier.alertupdate.notificationDelaysInSeconds}")
  private Integer[] notificationDelaysInSeconds;

  private NotificationService notificationService;
  private TaskScheduler taskScheduler;
  private EmployeeFeedbackService employeeFeedbackService;
  private SkillRepository skillRepository;
  private EmployeeSkillRepository employeeSkillRepository;

  public AlertNotifier(NotificationService notificationService,
      TaskScheduler taskScheduler,
      EmployeeFeedbackService employeeFeedbackService,
      SkillRepository skillRepository,
      EmployeeSkillRepository employeeSkillRepository) {
    this.notificationService = notificationService;
    this.taskScheduler = taskScheduler;
    this.employeeFeedbackService = employeeFeedbackService;
    this.skillRepository = skillRepository;
    this.employeeSkillRepository = employeeSkillRepository;
  }

  public List<ScheduledFuture<?>> notify(Alert alert) {
    List<ScheduledFuture<?>> futures = new ArrayList<>();

    logger.info("New AlertCreated notification registered on Scheduler: Immediately");
    ScheduledFuture<?> alertCreatedScheduledFuture = registerOnceScheduledTask(
        () -> alertCreated(alert),
        getNextExecutionDate(0));
    futures.add(alertCreatedScheduledFuture);

    for (Integer notificationDelayInSeconds : notificationDelaysInSeconds) {
      if (notificationDelayInSeconds == null) {
        continue;
      }

      Date nextExecutionTime = getNextExecutionDate(notificationDelayInSeconds);
      String nextExecutionTimeAsISO = asIso(nextExecutionTime);

      logger.info("New AlertFeedback notification registered on Scheduler: Delay {}s (Effective: {})",
          notificationDelayInSeconds,
          nextExecutionTimeAsISO);

      ScheduledFuture<?> scheduledFuture = registerOnceScheduledTask(
          () -> alertStatusUpdate(alert, notificationDelayInSeconds, nextExecutionTimeAsISO),
          nextExecutionTime);
      futures.add(scheduledFuture);
    }

    return futures;
  }

  private String asIso(Date nextExecutionTime) {
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    df.setTimeZone(TimeZone.getTimeZone("UTC"));
    return df.format(nextExecutionTime);
  }

  private Date getNextExecutionDate(Integer notificationDelayInSeconds) {
    Calendar nextExecutionTime = new GregorianCalendar();
    nextExecutionTime.setTime(new Date());
    nextExecutionTime.add(Calendar.SECOND, notificationDelayInSeconds);
    return nextExecutionTime.getTime();
  }

  private void alertCreated(Alert alert) {
    logger.info("Build AlertCreated Notification");
    Message message = buildAlertCreatedMessage(alert);
    notificationService.send(message);
  }

  public Message buildAlertCreatedMessage(Alert alert) {
    String subject = "Alarm:" + alert.getKeyword() + " (" + alert.getReferenceId() + ")";

    String address = alert.getAddress();
    if (!StringUtils.isBlank(alert.getAddressInfo1()) && !StringUtils.isBlank(alert.getAddressInfo2())) {
      address = alert.getAddressInfo1() + "\n" + alert.getAddressInfo2();
    } else if(!StringUtils.isBlank(alert.getAddressInfo1()) || !StringUtils.isBlank(alert.getAddressInfo2())){
      address = alert.getAddressInfo1() + alert.getAddressInfo2();
    }

    String description = StringUtils.isBlank(alert.getDescription()) ? "" : alert.getDescription() + "\n";

    String body = alert.getKeyword() + "\n"
        + description
        + address;

    return new Message(alert.getOrganisationId(), ALERT_CREATED, subject, body);
  }

  private void alertStatusUpdate(Alert alert, Integer notificationDelayInSeconds, String nextExecutionTimeAsISO) {
    logger.info("Build AlertFeedback Notification (Delay was {}s, effective: {})",
        notificationDelayInSeconds,
        nextExecutionTimeAsISO
    );
    Message message = buildAlertStatusUpdateMessage(alert);
    notificationService.send(message);
  }

  public Message buildAlertStatusUpdateMessage(Alert alert) {
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
    if(skillCount.size() > 0) {
      skillBodyPart.append("\n\nKOMMEN pro Skill:");
      for (Entry<String, Integer> skillCountEntry : skillCount.entrySet()) {
        skillBodyPart.append("\n");
        skillBodyPart.append("* ");
        skillBodyPart.append(skillCountEntry.getKey());
        skillBodyPart.append(": ");
        skillBodyPart.append(skillCountEntry.getValue());
      }
    }

    String subject = "Feedback for Alarm:" + alert.getKeyword() + " (" + alert.getReferenceId() + ")";
    String body = "KOMMEN:" + employeeIdsThatCommitted.size() + "\n"
        + "ABGELEHNT:" + employeeIdsThatCancel.size()
        + skillBodyPart.toString();

    return new Message(alert.getOrganisationId(), ALERT_UPDATE, subject, body);
  }

  private Map<String, Integer> groupByEmployeeSkillsAndCount(Long organisationId, List<Long> employeeIdsThatCommitted) {
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

  private ScheduledFuture<?> registerOnceScheduledTask(Runnable task, Date nextExecutionTime) {
    return taskScheduler.schedule(task, nextExecutionTime);
  }
}
