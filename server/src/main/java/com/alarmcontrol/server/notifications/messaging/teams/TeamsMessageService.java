package com.alarmcontrol.server.notifications.messaging.teams;

import com.alarmcontrol.server.notifications.core.messaging.AbstractMessageService;
import com.alarmcontrol.server.notifications.core.messaging.Message;
import com.alarmcontrol.server.notifications.messaging.mail.MailContact;
import com.alarmcontrol.server.utils.DateToIsoFormatter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.io.StringWriter;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TeamsMessageService extends AbstractMessageService<TeamsContact> {

  private Logger logger = LoggerFactory.getLogger(TeamsMessageService.class);
  private RestTemplate restTemplate;
  private RetryTemplate retryTemplate;

  private static final String MESSAGE_TEMPLATE = "{\n" +
          "\t\"@type\": \"MessageCard\",\n" +
          "\t\"@context\": \"https://schema.org/extensions\",\n" +
          "\t\"summary\": {{SUBJECT}},\n" +
          "\t\"themeColor\": \"0078D7\",\n" +
          "\t\"title\": {{SUBJECT}},\n" +
          "\t\"sections\": [\n" +
          "\t\t{\n" +
          "\t\t\t\"activityTitle\": \"\",\n" +
          "\t\t\t\"activitySubtitle\": \"\",\t\t\t\n" +
          "\t\t\t\"facts\": [\n" +
          "\t\t\t\t{\n" +
          "\t\t\t\t\t\"name\": \"Type:\",\n" +
          "\t\t\t\t\t\"value\": {{SEVERITY}}\n" +
          "\t\t\t\t},\n" +
          "\t\t\t\t{\n" +
          "\t\t\t\t\t\"name\": \"SentAtUTC:\",\n" +
          "\t\t\t\t\t\"value\": {{DATE}}\n" +
          "\t\t\t\t}\n" +
          "\t\t\t],\n" +
          "\t\t\t\"text\": {{BODY}}\n" +
          "\t\t}\n" +
          "\t],\n" +
          "\t\"potentialAction\": []\n" +
          "}";

  private static final String SUBJECT_PLACEHOLDER = "{{SUBJECT}}";
  private static final String BODY_PLACEHOLDER = "{{BODY}}";
  private static final String SEVERITY_PLACEHOLDER = "{{SEVERITY}}";
  private static final String DATE_PLACEHOLDER = "{{DATE}}";

  public TeamsMessageService(RestTemplate restTemplate, RetryTemplate retryTemplate) {
    super(TeamsContact.class);
    this.restTemplate = restTemplate;
    this.retryTemplate = retryTemplate;
  }

  @Override
  protected void sendInternal(List<TeamsContact> contacts, Message message) {
    Assert.notNull(contacts, "contacts is null");
    Assert.notNull(message, "message is null");

    for (TeamsContact contact : contacts) {
      try {
        retryTemplate.execute(e -> sendMessage(contact, message));
      }catch (Exception e){
        logger.error("Can not send message to {} \ndue to: {}\n {}", contact, e.getMessage(), message, e);
      }
    }
  }

  private boolean sendMessage(TeamsContact contact, Message message) {
    if(StringUtils.isBlank(contact.getUrl())){
      throw new RuntimeException("Url is not set!");
    }

    logger.info("Start sending message '{}' via {}",
        message.getSubject(),
        message.getClass().getSimpleName());

    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    String subjectEncoded = jsonEncode(message.getSubject());
    String bodyEncoded = jsonEncode(message.getBody());
    String severityEncoded = jsonEncode(message.getSeverity().toString());
    String sentAtEncoded = jsonEncode(DateTimeFormatter.ISO_INSTANT.format(Instant.now()));

    bodyEncoded = bodyEncoded.replace("\\n", "\\n\\n");

    String json = MESSAGE_TEMPLATE
            .replace(SUBJECT_PLACEHOLDER, subjectEncoded)
            .replace(BODY_PLACEHOLDER, bodyEncoded)
            .replace(SEVERITY_PLACEHOLDER, severityEncoded)
            .replace(DATE_PLACEHOLDER, sentAtEncoded);

    HttpEntity<String> entity = new HttpEntity<>(json, headers);
    ResponseEntity<String> result = restTemplate.exchange(contact.getUrl(), HttpMethod.POST, entity, String.class);

    logger.info("Following message sent\n: {}", message);

    return true;
  }

  private String jsonEncode(String value){
    try{
      ObjectMapper mapper = new ObjectMapper();
      StringWriter writer = new StringWriter();
      mapper.writeValue(writer, value);
      return writer.toString();
    }catch (Exception e){
      throw new RuntimeException(e);
    }
  }

  private String asIso(Date date) {
    return DateToIsoFormatter.asIso(date);
  }
}
