package com.alarmcontrol.server.notifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class NotificationService {

  private Logger logger = LoggerFactory.getLogger(NotificationService.class);
  private MailNotificationService mailNotificationService;

  public NotificationService(MailNotificationService mailNotificationService) {
    this.mailNotificationService = mailNotificationService;
  }

  public void send(Message message){
    Assert.notNull(message, "message is null");
    logger.info("Start sending {}", message);
    try{
      mailNotificationService.send(message);
    }catch (Exception e){
      logger.error("Can not send notification: {}\n {}", e.getMessage(), message, e);
    }
  }
}
