package com.alarmcontrol.server.notifications.core.messaging;

import com.alarmcontrol.server.notifications.core.config.Contact;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class MessageService {
  private Logger logger = LoggerFactory.getLogger(MessageService.class);
  private List<AbstractMessageService> messageServices;

  public MessageService(List<AbstractMessageService> messageServices) {
    this.messageServices = messageServices;
  }

  public void send(Message message, List<Contact> contacts){
    Assert.notNull(message, "message is null");

    for (Contact contact : contacts) {
      boolean messageServiceFound = false;

      for (AbstractMessageService messageService : messageServices) {
        if(messageService.isResponsible(contact)){
          messageServiceFound = true;
          sendMessage(message, contact, messageService);
          break;
        }
      }

      if(!messageServiceFound){
        logger.error("Can not find any MessageService to sent "
                + "a message to a contact of type {}",
            contact.getClass().getSimpleName());
      }
    }
  }

  private void sendMessage(Message message, Contact contact, AbstractMessageService messageService) {
    logger.info("Start sending message '{}' via {} to {} ",
        message.getSubject(),
        message.getClass().getSimpleName(),
        contact);
    try{
      messageService.send(contact, message);
      logger.info("Following message sent to {}\n: {}",
          contact,
          message);
    }catch (Exception e){
      logger.error("Can not send message due to: {}\n {} \n {}", e.getMessage(), contact, message, e);
    }
  }
}
