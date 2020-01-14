package com.alarmcontrol.server.notifications.messaging.mail;

import com.alarmcontrol.server.notifications.core.messaging.AbstractMessageService;
import com.alarmcontrol.server.notifications.core.messaging.Message;
import com.alarmcontrol.server.notifications.core.messaging.MessageService;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class MailMessageService extends AbstractMessageService<MailContact> {

  private Logger logger = LoggerFactory.getLogger(MailMessageService.class);

  @Value("${spring.mail.host:}")
  private String host;

  @Value("${spring.mail.port:}")
  private String port;

  @Value("${spring.mail.properties.mail.smtp.auth:}")
  private String useAuth;

  @Value("${spring.mail.username:}")
  private String username;

  @Value("${spring.mail.password:}")
  private String password;

  private JavaMailSender javaMailSender;

  public MailMessageService(JavaMailSender javaMailSender) {
    super(MailContact.class);
    this.javaMailSender = javaMailSender;
  }

  @Override
  protected void sendInternal(List<MailContact> contacts, Message message) {
    Assert.notNull(contacts, "contacts is null");
    Assert.notNull(message, "message is null");

    if(StringUtils.isBlank(host)){
      throw new RuntimeException("Host is not set!");
    }

    if(StringUtils.isBlank(port)){
      throw new RuntimeException("Port is not set!");
    }

    if(useAuthForMailserver()){
      if(StringUtils.isBlank(username)){
        throw new RuntimeException("Auth is enabled, but username is not set!");
      }

      if(StringUtils.isBlank(password)){
        throw new RuntimeException("Auth is enabled, but password is not set!");
      }
    }

    for (MailContact contact : contacts) {
      sendEmail(contact, message);
    }
  }

  private Boolean useAuthForMailserver() {
    try{
      return Boolean.parseBoolean(useAuth);
    }catch(Exception e) {
      return false;
    }
  }

  private void sendEmail(MailContact contact, Message message) {
    try{
      if(StringUtils.isBlank(contact.getMailAddress())){
        throw new RuntimeException("Mailaddress is not set!");
      }

      logger.info("Start sending message '{}' via {} to {}",
          message.getSubject(),
          message.getClass().getSimpleName(),
          contact.getMailAddress());

      SimpleMailMessage msg = new SimpleMailMessage();
      msg.setTo(contact.getMailAddress());

      msg.setSubject(message.getSubject());
      msg.setText(message.getBody());

      javaMailSender.send(msg);

      logger.info("Following message sent to {} \n: {}",
          contact.getMailAddress(),
          message);

    }catch (Exception e){
      logger.error("Can not send message to {} \ndue to: {}\n {}", contact, e.getMessage(), message, e);
    }
  }
}
