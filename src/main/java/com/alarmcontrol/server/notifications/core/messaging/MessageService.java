package com.alarmcontrol.server.notifications.core.messaging;

import com.alarmcontrol.server.notifications.core.config.Contact;
import com.alarmcontrol.server.notifications.messaging.mail.MailMessageService;
import com.alarmcontrol.server.notifications.messaging.mail.MailContact;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class MessageService {
  private Logger logger = LoggerFactory.getLogger(MessageService.class);
  private MailMessageService mailMessageService;

  public MessageService(MailMessageService mailMessageService) {
    this.mailMessageService = mailMessageService;
  }

  public void send(Message message, List<Contact> contacts){
    Assert.notNull(message, "message is null");

    for (Contact contact : contacts) {
      logger.info("Start sending message with body '{}' to {}",
          message.getSubject(),
          contact);

      if(contact instanceof MailContact){
        sendMail((MailContact)contact, message);
      }else{
        logger.error("Can not send message to contact of type {}, due to it is not a known ContactType",
            contact.getClass().getSimpleName());
        return;
      }

      logger.info("Following message sent to {}\n: {}",
          contact,
          message);
    }
  }

  private void sendMail(MailContact mailContact, Message message) {
    try{
      mailMessageService.send(mailContact.getMailAddress(), message);
    }catch (Exception e){
      logger.error("Can not send mail due to: {}\n {}", e.getMessage(), message, e);
    }
  }
}
