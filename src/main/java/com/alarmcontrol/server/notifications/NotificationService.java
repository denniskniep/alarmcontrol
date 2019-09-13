package com.alarmcontrol.server.notifications;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class NotificationService {

  public void send(Message message){
    Assert.notNull(message, "message is null");
  }
}
