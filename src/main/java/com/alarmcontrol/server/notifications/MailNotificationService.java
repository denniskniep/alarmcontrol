package com.alarmcontrol.server.notifications;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class MailNotificationService {
  private Logger logger = LoggerFactory.getLogger(MailNotificationService.class);

  @Value("${notifier.mail.to:}")
  private String mailaddress;

  @Value("${spring.mail.host}")
  private String host;

  @Value("${spring.mail.port}")
  private String port;

  @Value("${spring.mail.properties.mail.smtp.auth}")
  private String useAuth;

  @Value("${spring.mail.username}")
  private String username;

  @Value("${spring.mail.password}")
  private String password;

  private JavaMailSender javaMailSender;

  public MailNotificationService(JavaMailSender javaMailSender) {
    this.javaMailSender = javaMailSender;
  }

  public void send(Message message){
    Assert.notNull(message, "message is null");

    if(StringUtils.isBlank(host)){
      throw new RuntimeException("Host is not set!");
    }

    if(StringUtils.isBlank(port)){
      throw new RuntimeException("Port is not set!");
    }

    if(StringUtils.isBlank(mailaddress)){
      throw new RuntimeException("Mailaddress is not set!");
    }

    if(getUseAuth()){
      if(StringUtils.isBlank(username)){
        throw new RuntimeException("Auth is enabled, but username is not set!");
      }

      if(StringUtils.isBlank(password)){
        throw new RuntimeException("Auth is enabled, but password is not set!");
      }
    }

    sendEmail(message);
    logger.info("Sent the following notification Mail to {}: {}", mailaddress, message);
  }

  private Boolean getUseAuth() {
    try{
      return Boolean.parseBoolean(useAuth);
    }catch(Exception e) {
      return false;
    }
  }

  private void sendEmail(Message message) {
    SimpleMailMessage msg = new SimpleMailMessage();
    msg.setTo(mailaddress);

    msg.setSubject(message.getSubject());
    msg.setText(message.getBody());

    javaMailSender.send(msg);
  }
}
