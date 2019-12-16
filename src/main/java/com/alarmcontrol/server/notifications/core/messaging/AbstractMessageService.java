package com.alarmcontrol.server.notifications.core.messaging;

import com.alarmcontrol.server.notifications.core.config.Contact;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractMessageService<T extends Contact> {

  private Class<T> clazz;

  public AbstractMessageService(Class<T> clazz) {
    this.clazz = clazz;
  }

  public boolean isResponsible(Contact contact){
    return clazz.isInstance(contact);
  }

  public void send(List<Contact> contacts, Message message){
    List<T> casted = contacts
        .stream()
        .filter(c -> isResponsible(c))
        .map(c -> (T)c)
        .collect(Collectors.toList());

    sendInternal(casted, message);
  }

  protected abstract void sendInternal(List<T> contacts, Message message);
}
