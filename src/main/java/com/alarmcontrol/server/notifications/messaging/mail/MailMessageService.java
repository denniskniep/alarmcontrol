package com.alarmcontrol.server.notifications.messaging.mail;

import com.alarmcontrol.server.notifications.core.messaging.AbstractMessageService;
import com.alarmcontrol.server.notifications.core.messaging.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class MailMessageService extends AbstractMessageService<MailContact> {
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
  protected void sendInternal(MailContact contact, Message message) {
    Assert.notNull(contact, "contact is null");
    Assert.notNull(message, "message is null");

    if(StringUtils.isBlank(host)){
      throw new RuntimeException("Host is not set!");
    }

    if(StringUtils.isBlank(port)){
      throw new RuntimeException("Port is not set!");
    }

    if(getUseAuth()){
      if(StringUtils.isBlank(username)){
        throw new RuntimeException("Auth is enabled, but username is not set!");
      }

      if(StringUtils.isBlank(password)){
        throw new RuntimeException("Auth is enabled, but password is not set!");
      }
    }

    if(StringUtils.isBlank(contact.getMailAddress())){
      throw new RuntimeException("Mailaddress is not set!");
    }

    sendEmail(contact.getMailAddress(), message);
  }

  private Boolean getUseAuth() {
    try{
      return Boolean.parseBoolean(useAuth);
    }catch(Exception e) {
      return false;
    }
  }

  private void sendEmail(String mailaddress, Message message) {
    SimpleMailMessage msg = new SimpleMailMessage();
    msg.setTo(mailaddress);

    msg.setSubject(message.getSubject());
    msg.setText(message.getBody());

    javaMailSender.send(msg);
  }
}
