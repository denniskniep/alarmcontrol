package com.alarmcontrol.server.notifications.messaging.firebasepush;

import com.alarmcontrol.server.notifications.core.messaging.AbstractMessageService;
import com.alarmcontrol.server.notifications.core.messaging.Message;
import com.alarmcontrol.server.utils.DateToIsoFormatter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class FirebaseMessageService extends AbstractMessageService<FirebaseMessageContact> {

  public static final int TOKENS_CACHE_TIMEOUT_IN_MS = 5000;
  private Logger logger = LoggerFactory.getLogger(FirebaseMessageService.class);

  @Value("${notifications.firebase.push.url:}")
  private String pushUrl;

  @Value("${notifications.firebase.auth.url:}")
  private String authUrl;

  @Value("${notifications.firebase.database.url:}")
  private String databaseUrl;

  @Value("${notifications.firebase.push.authorizationHeader:}")
  private String pushAuthorizationHeader;

  @Value("${notifications.firebase.username:}")
  private String username;

  @Value("${notifications.firebase.password:}")
  private String password;

  @Value("${notifications.firebase.projectId:}")
  private String projectId;

  @Value("${notifications.firebase.publicApiKey:}")
  private String publicApiKey;

  private RestTemplate restTemplate;

  private Map<String, String> tokensByMailCache;
  private Date lastCacheFetch;

  public FirebaseMessageService(RestTemplate restTemplate) {
    super(FirebaseMessageContact.class);
    this.restTemplate = restTemplate;
  }

  @Override
  protected void sendInternal(List<FirebaseMessageContact> contacts, Message message) {
    Assert.notNull(contacts, "contacts is null");
    Assert.notNull(message, "message is null");

    if(StringUtils.isBlank(pushUrl)){
      throw new RuntimeException("Url is not set!");
    }

    if(StringUtils.isBlank(pushAuthorizationHeader)){
      throw new RuntimeException("AuthorizationHeader is not set!");
    }

    Map<String, String> tokensByMail = getTokensByMailFromCacheAndRefreshCacheIfNecessary();
    for (FirebaseMessageContact contact : contacts) {
      sendFirebaseMessage(contact, message, tokensByMail);
    }
  }

  private synchronized Map<String, String> getTokensByMailFromCacheAndRefreshCacheIfNecessary() {
    boolean cacheTimedOut = false;
    if(lastCacheFetch != null){
      long lastCacheFetchInMs = lastCacheFetch.getTime();
      long dateInMs = new Date().getTime();
      long cacheAgeInMs = dateInMs - lastCacheFetchInMs;
      cacheTimedOut = cacheAgeInMs > TOKENS_CACHE_TIMEOUT_IN_MS;
      if(cacheTimedOut){
        logger.info("TokensByMail Cache timed out because it is older than {}ms (LastCacheFetch:{})", TOKENS_CACHE_TIMEOUT_IN_MS, cacheAgeInMs);
      }
    }

    if(tokensByMailCache == null || lastCacheFetch == null || cacheTimedOut){
      AuthResult authResult = login();
      tokensByMailCache = getTokensByMailFromDatastore(authResult);
      lastCacheFetch = new Date();
      logger.info("Updated TokensByMail Cache. Now it contains {} entries", tokensByMailCache.size());
    }else{
      logger.info("Using existing TokensByMail Cache. It contains {} entries", tokensByMailCache.size());
    }

    return new HashMap<>(tokensByMailCache);
  }

  private AuthResult login() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

    Map<String, Object> bodyWithCredentials = new HashMap<>();
    bodyWithCredentials.put("email",  username);
    bodyWithCredentials.put("password", password);
    bodyWithCredentials.put("returnSecureToken", true);

    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(authUrl + "/accounts:signInWithPassword");
    builder.queryParam("key",  publicApiKey);
    URI uri = builder.build().encode().toUri();

    HttpEntity<Map<String,Object>> dataEntity = new HttpEntity<>(bodyWithCredentials, headers);

    ResponseEntity<AuthResult> result = restTemplate.exchange(uri, HttpMethod.POST, dataEntity, AuthResult.class);
    return result.getBody();
  }

  private Map<String, String> getTokensByMailFromDatastore(AuthResult authResult) {
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + authResult.getIdToken());

    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(databaseUrl + "/projects/{projectId}/databases/{database}/documents/{collection}");
    Map<String, Object> parameter = new HashMap<>();
    parameter.put("projectId",  projectId);
    parameter.put("database",  "(default)");
    parameter.put("collection", "subscriptiontokens");
    URI uri = builder.buildAndExpand(parameter).encode().toUri();

    HttpEntity<Map<String,Object>> dataEntity = new HttpEntity<>(new HashMap<>(), headers);

    ResponseEntity<JsonNode> result = restTemplate.exchange(uri, HttpMethod.GET, dataEntity, JsonNode.class);
    Map<String, String> mailToToken = new HashMap<>();

    JsonNode root = result.getBody();
    ArrayNode documents = (ArrayNode)root.get("documents");
    for (int i = 0; i < documents.size(); i++) {
      try{
        JsonNode document = documents.get(i);
        String email = document.get("fields").get("email").get("stringValue").textValue();
        String message_token = document.get("fields").get("message_token").get("stringValue").textValue();
        mailToToken.put(email, message_token);
      }catch (Exception e){
        logger.error("Skipping Token Mapping! Can not read token mapping at index {} due to {}", i, e.getMessage(), e);
      }
    }

    logger.info("Found {} Tokens in Database", mailToToken.size());
    return mailToToken;
  }

  private void sendFirebaseMessage(FirebaseMessageContact contact, Message message, Map<String, String> tokensByMail) {
    try{
      if(StringUtils.isBlank(contact.getToken())){
        throw new RuntimeException("Token is not set!");
      }
      logger.info("Start sending message '{}' via {} to Mail {}",
          message.getSubject(),
          message.getClass().getSimpleName(),
          contact.getToken());

      String token = tokensByMail.get(contact.getToken());
      logger.info("Found Token {} for mail {}",
          token,
          contact.getToken());

      sendFirebaseMessageToToken(token, "notification", message);
      Thread.sleep(1000);
      sendFirebaseMessageToToken(token, "data", message);

      logger.info("Following message sent to Mail {} ({}) \n: {}",
          contact.getToken(),
          token,
          message);

    }catch (Exception e){
      logger.error("Can not send message to {} \ndue to: {}\n {}", contact, e.getMessage(), message, e);
    }
  }

  private void sendFirebaseMessageToToken(String token, String messageKind, Message message) {
    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set(HttpHeaders.AUTHORIZATION, "key=" + pushAuthorizationHeader);

    Map<String, Object> messageWrapper = createMessageWrapper(token);
    messageWrapper.put(messageKind, createMessage(message));
    final HttpEntity<Map<String,Object>> notificationEntity = new HttpEntity<>(messageWrapper, headers);
    restTemplate.exchange(pushUrl, HttpMethod.POST, notificationEntity, String.class);
  }

  private Map<String, Object> createMessage(Message message) {
    Map<String, Object> dataMessage = new HashMap<>();
    dataMessage.put("title", message.getSubject());
    dataMessage.put("body", message.getBody());
    dataMessage.put("type", message.getSeverity());
    dataMessage.put("sentAt", asIso(new Date()));
    return dataMessage;
  }

  private String asIso(Date date) {
    return DateToIsoFormatter.asIso(date);
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

  private static class AuthResult{

    private String idToken;

    public String getIdToken() {
      return idToken;
    }

    public void setIdToken(String idToken) {
      this.idToken = idToken;
    }
  }
}
