package com.alarmcontrol.server.maps;

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
  private ConcurrentHashMap<String, ResponseEntity<Object>> cache;

  public CachingRestService(@Qualifier("mapRestTemplate") RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
    this.cache = new ConcurrentHashMap<>();
  }

  public Response executeRequest(Request request) {
    if(cache.containsKey(request.getKeyForCache())){
      logger.info("Using result from cache");
      return new Response(true, cache.get(request.getKeyForCache()));
    }
    ResponseEntity<Object> result = exchange(request);
    cacheRequest(request.getKeyForCache(), result);
    return new Response(false, result);
  }

  private ResponseEntity<Object> exchange(Request request){
    logger.info("Start request");
    ResponseEntity<Object> result = restTemplate.exchange(request.getUri(),
        request.getMethod(),
        request.getEntity(),
        Object.class);

    if(result.getStatusCode().isError()){
      String errMessage = "Error during request. "
          + "Uri="+request.getUri()
          + ", StatusCode="+result.getStatusCodeValue()
          + ", Body="+result.getBody().toString();
      logger.error(errMessage);
      throw new RuntimeException(errMessage);
    }

    logger.info("Request successful");
    return result;
  }

  private void cacheRequest(String key, ResponseEntity<Object> result) {
    logger.info("Caching result");
    cache.put(key, result);
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

    public Response(boolean fromCache, ResponseEntity<Object> response) {
      this.fromCache = fromCache;
      this.response = response;
    }

    public boolean isFromCache() {
      return fromCache;
    }

    public ResponseEntity<Object> getResponse() {
      return response;
    }
  }
}
