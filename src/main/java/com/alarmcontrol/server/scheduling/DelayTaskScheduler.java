package com.alarmcontrol.server.scheduling;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

@Service
public class DelayTaskScheduler {
  private Logger logger = LoggerFactory.getLogger(DelayTaskScheduler.class);
  private TaskScheduler taskScheduler;

  private List<ScheduledFuture<?>> scheduledTasks;

  public DelayTaskScheduler(TaskScheduler taskScheduler) {
    this.taskScheduler = taskScheduler;
    this.scheduledTasks = new ArrayList<>();
  }

  public ScheduledFuture<?> registerOnceScheduledTask(Runnable task, int delayInSeconds) {
    Date nextExecutionTime = getNextExecutionDate(delayInSeconds);
    String nextExecutionTimeAsISO = asIso(nextExecutionTime);

    ScheduledFuture<?> handle = registerOnceScheduledTask(
        () -> execute(task, delayInSeconds, nextExecutionTimeAsISO), nextExecutionTime);

    addScheduled(handle);

    logger.info("New ScheduledTask registered on Scheduler: Delay {}s (EffectiveDate: {})",
        delayInSeconds,
        nextExecutionTimeAsISO);

    return handle;
  }

  private synchronized void addScheduled(ScheduledFuture<?> scheduledFuture){
    scheduledTasks.add(scheduledFuture);
  }

  public synchronized List<ScheduledFuture<?>> getPendingTasks(){
    for (ScheduledFuture<?> scheduledFuture : new ArrayList<>(scheduledTasks)) {
      if(scheduledFuture.isDone() || scheduledFuture.isCancelled()){
        scheduledTasks.remove(scheduledFuture);
      }
    }
    return scheduledTasks;
  }

  private void execute(Runnable task, int delayInSeconds, String nextExecutionTimeAsISO) {
    logger.info("Start executing scheduled task (Delay was {}s EffectiveDate was {})",
        delayInSeconds,
        nextExecutionTimeAsISO);

    task.run();

    logger.info("Finished executing scheduled task",
        delayInSeconds,
        nextExecutionTimeAsISO);
  }

  private ScheduledFuture<?> registerOnceScheduledTask(Runnable task, Date nextExecutionTime) {
    return taskScheduler.schedule(task, nextExecutionTime);
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
}
