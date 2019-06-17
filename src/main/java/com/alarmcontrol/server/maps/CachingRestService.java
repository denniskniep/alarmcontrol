package com.alarmcontrol.server.maps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component()
@Scope("prototype")
public class CachingRestService {
  private Logger logger = LoggerFactory.getLogger(CachingRestService.class);
  private RestTemplate restTemplate;
  private ConcurrentHashMap<String, String> cache;
  private ObjectMapper objectMapper;

  public CachingRestService(@Qualifier("mapRestTemplate") RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
    this.cache = new ConcurrentHashMap<>();
    this.objectMapper = new ObjectMapper();
  }

  public Response executeRequest(Request request) {
    if(cache.containsKey(request.getKeyForCache())){
      logger.info("Using result from cache");
      String cachedJson = cache.get(request.getKeyForCache());
      return new Response(true, null, cachedJson);
    }
    ResponseEntity<Object> result = exchange(request);
    String json =  asJsonString(result);
    cacheRequest(request.getKeyForCache(), json);
    return new Response(false, result, json);
  }

  private String asJsonString(ResponseEntity<Object> result) {
    try {
      String json = objectMapper.writeValueAsString(result.getBody());
      logger.debug("Serialized body into following json:\n" + json);
      return json;
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Can not serialize the following response body into json:\n"+result.getBody(), e);
    }
  }

  private ResponseEntity<Object> exchange(Request request){
    logger.info("Start request");
    ResponseEntity<Object> result = restTemplate.exchange(request.getUri(),
        request.getMethod(),
        request.getEntity(),
        Object.class);

    logger.info("Request successful, StatusCode=" + result.getStatusCodeValue());
    return result;
  }

  private void cacheRequest(String key, String json) {
    logger.info("Caching result");
    cache.put(key, json);
  }

  public static class Request{
    private String keyForCache;
    private URI uri;
    private HttpMethod method;
    private HttpEntity<Object> entity;

    public Request(String keyForCache, URI uri, HttpMethod method, HttpEntity<Object> entity) {
      this.keyForCache = keyForCache;
      this.uri = uri;
      this.method = method;
      this.entity = entity;
    }

    public String getKeyForCache() {
      return keyForCache;
    }

    public URI getUri() {
      return uri;
    }

    public HttpMethod getMethod() {
      return method;
    }

    public HttpEntity<Object> getEntity() {
      return entity;
    }
  }

  public static class Response{
    private boolean fromCache;
    private ResponseEntity<Object> response;
    private String json;

    public Response(boolean fromCache, ResponseEntity<Object> response, String json) {
      this.fromCache = fromCache;
      this.response = response;
      this.json = json;
    }

    public boolean isFromCache() {
      return fromCache;
    }

    /**
     * The response is empty, if it is retrieved from cache
     * @return
     */
    public ResponseEntity<Object> getResponse() {
      return response;
    }

    public String getJson() {
      return json;
    }
  }
}
