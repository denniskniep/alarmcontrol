package com.alarmcontrol.server.notifications.core.messaging;

import com.alarmcontrol.server.notifications.core.config.Contact;

public abstract class AbstractMessageService<T extends Contact> {

  private Class<T> clazz;

  public AbstractMessageService(Class<T> clazz) {
    this.clazz = clazz;
  }

  public boolean isResponsible(Contact contact){
    return clazz.isInstance(contact);
  }

  public void send(Contact contact, Message message){
    if(isResponsible(contact)){
      sendInternal((T)contact, message);
    }
  }

  protected abstract void sendInternal(T contact, Message message);
}
