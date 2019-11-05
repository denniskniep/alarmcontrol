package com.alarmcontrol.server.notifications.core.messaging;

import com.alarmcontrol.server.notifications.core.config.Contact;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.LinkedCaseInsensitiveMap;

@Service
public class MessageService {
  private Logger logger = LoggerFactory.getLogger(MessageService.class);
  private List<AbstractMessageService> messageServices;

  public MessageService(List<AbstractMessageService> messageServices) {
    this.messageServices = messageServices;
  }

  public void send(Message message, List<Contact> contacts){
    Assert.notNull(message, "message is null");

    Map<AbstractMessageService, List<Contact>> serviceToContacts =  groupContactsByResponsibleMessageService(contacts);

    for (Entry<AbstractMessageService, List<Contact>> serviceToContactsEntry : serviceToContacts.entrySet()) {
      AbstractMessageService service = serviceToContactsEntry.getKey();
      List<Contact> contactsForService = serviceToContactsEntry.getValue();

      sendMessage(message, contactsForService, service);
    }
  }

  private Map<AbstractMessageService, List<Contact>> groupContactsByResponsibleMessageService(List<Contact> contacts) {
    Map<AbstractMessageService, List<Contact>> serviceToContacts = new HashMap<>();
    for (Contact contact : contacts) {
      boolean messageServiceFound = false;

      for (AbstractMessageService messageService : messageServices) {
        if(messageService.isResponsible(contact)){
          messageServiceFound = true;
          if(!serviceToContacts.containsKey(messageService)){
            serviceToContacts.put(messageService, new ArrayList<>());
          }

          List<Contact> contactsForService = serviceToContacts.get(messageService);
          contactsForService.add(contact);
          break;
        }
      }

      if(!messageServiceFound){
        logger.error("Can not find any MessageService to sent "
                + "a message to a contact of type {}",
            contact.getClass().getSimpleName());
      }
    }
    return serviceToContacts;
  }

  private void sendMessage(Message message, List<Contact> contacts, AbstractMessageService messageService) {
    logger.info("Start sending message '{}' via {}",
        message.getSubject(),
        message.getClass().getSimpleName());
    try{
      messageService.send(contacts, message);
    }catch (Exception e){
      logger.error("Can not send message due to: {}\n {}", e.getMessage(), message, e);
    }
  }
}
