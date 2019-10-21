package com.alarmcontrol.server.notifications.messaging.firebasepush;

import com.alarmcontrol.server.notifications.core.messaging.AbstractMessageService;
import com.alarmcontrol.server.notifications.core.messaging.Message;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

@Service
public class FirebaseMessageService extends AbstractMessageService<FirebaseMessageContact> {

  @Value("${notifications.firebase.url:}")
  private String url;

  @Value("${notifications.firebase.AuthorizationHeader:}")
  private String authorizationHeader;

  private RestTemplate restTemplate;

  public FirebaseMessageService(RestTemplate restTemplate) {
    super(FirebaseMessageContact.class);
    this.restTemplate = restTemplate;
  }

  @Override
  protected void sendInternal(FirebaseMessageContact contact, Message message) {
    Assert.notNull(contact, "contact is null");
    Assert.notNull(message, "message is null");

    if(StringUtils.isBlank(url)){
      throw new RuntimeException("Url is not set!");
    }

    if(StringUtils.isBlank(authorizationHeader)){
      throw new RuntimeException("AuthorizationHeader is not set!");
    }

    if(StringUtils.isBlank(contact.getToken())){
      throw new RuntimeException("Token is not set!");
    }

    sendFirebaseMessage(contact.getToken(), message);
  }

  private void sendFirebaseMessage(String token, Message message) {
    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set(HttpHeaders.AUTHORIZATION, "key=" + authorizationHeader);

    Map<String, Object> notificationMessage = createMessageWrapper(token);
    notificationMessage.put("notification", createMessage(message));
    final HttpEntity<Map<String,Object>> notificationEntity = new HttpEntity<>(notificationMessage, headers);
    restTemplate.exchange(url, HttpMethod.POST, notificationEntity, String.class);

    Map<String, Object> dataMessage = createMessageWrapper(token);
    dataMessage.put("data", createMessage(message));
    final HttpEntity<Map<String,Object>> dataEntity = new HttpEntity<>(dataMessage, headers);
    restTemplate.exchange(url, HttpMethod.POST, dataEntity, String.class);
  }

  private Map<String, Object> createMessage(Message message) {
    Map<String, Object> dataMessage = new HashMap<>();
    dataMessage.put("title", message.getSubject());
    dataMessage.put("body", message.getBody());
    return dataMessage;
  }

  private Map<String, Object> createMessageWrapper(String token) {
    Map<String, Object> dataMessage = new HashMap<>();
    dataMessage.put("to", token);
    dataMessage.put("priority", "high");

    Map<String, Object> android = new HashMap<>();
    android.put("priority",  "high");
    dataMessage.put("android", android);

    return dataMessage;
  }
}
