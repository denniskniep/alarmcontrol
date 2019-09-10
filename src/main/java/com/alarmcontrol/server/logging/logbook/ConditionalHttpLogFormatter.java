package com.alarmcontrol.server.logging.logbook;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.zalando.logbook.Correlation;
import org.zalando.logbook.DefaultHttpLogFormatter;
import org.zalando.logbook.HttpLogFormatter;
import org.zalando.logbook.HttpRequest;
import org.zalando.logbook.HttpResponse;
import org.zalando.logbook.Precorrelation;
import org.zalando.logbook.PreparedHttpLogFormatter;

public class ConditionalHttpLogFormatter implements HttpLogFormatter {

  public static final String REMOVED_BODY = "<<REMOVED BODY>>";

  private HttpMessageAsString httpMessageAsString;
  private LogbookCustomProperties properties;

  public ConditionalHttpLogFormatter(DefaultHttpLogFormatter logFormatter,
      LogbookCustomProperties properties) {
    this.httpMessageAsString = new DefaultHttpMessageAsString(logFormatter);
    this.properties = properties;
  }

  public ConditionalHttpLogFormatter(PreparedHttpLogFormatter logFormatter,
      LogbookCustomProperties properties) {
    this.httpMessageAsString = new PreparedHttpMessageAsString(logFormatter);
    this.properties = properties;
  }

  @Override
  public String format(Precorrelation<HttpRequest> precorrelation) throws IOException {    
    LogbookFilterProperties filterProperties = properties.getFilterProperties(precorrelation.getRequest());
    if(!shouldContainBody(precorrelation.getRequest(), filterProperties.getIncludeRequestBody(), filterProperties.getExcludeRequestBody()) &&
        !StringUtils.isBlank(precorrelation.getRequest().getBodyAsString())){
      return httpMessageAsString.withoutBody(precorrelation);
    }
    return httpMessageAsString.withBody(precorrelation);
  }

  @Override
  public String format(Correlation<HttpRequest, HttpResponse> correlation) throws IOException {
    LogbookFilterProperties filterProperties = properties.getFilterProperties(correlation.getResponse());
    if(!shouldContainBody(correlation.getRequest(), filterProperties.getIncludeResponseBody(), filterProperties.getExcludeResponseBody()) &&
        !StringUtils.isBlank(correlation.getResponse().getBodyAsString())){
      return httpMessageAsString.withoutBody(correlation);
    }
    return httpMessageAsString.withBody(correlation);
  }

  private boolean shouldContainBody(HttpRequest request, List<String> include, List<String> exclude) {
    return Conditions.isRelevant(request, include, exclude);
  }

  private interface HttpMessageAsString{
    String withBody(Precorrelation<HttpRequest> request) throws IOException;
    String withoutBody(Precorrelation<HttpRequest> request) throws IOException;

    String withBody(Correlation<HttpRequest, HttpResponse> response) throws IOException;
    String withoutBody(Correlation<HttpRequest, HttpResponse> response) throws IOException;
  }

  private class PreparedHttpMessageAsString implements HttpMessageAsString {

    private PreparedHttpLogFormatter preparedHttpLogFormatter;

    public PreparedHttpMessageAsString(PreparedHttpLogFormatter preparedHttpLogFormatter) {
      this.preparedHttpLogFormatter = preparedHttpLogFormatter;
    }

    @Override
    public String withBody(Precorrelation<HttpRequest> request) throws IOException {
      return preparedHttpLogFormatter.format(request);
    }

    @Override
    public String withoutBody(Precorrelation<HttpRequest> request) throws IOException {
      Map<String, Object> prepared = preparedHttpLogFormatter.prepare(request);
      if(prepared.containsKey("body")){
        prepared.put("body", REMOVED_BODY);
      }
      return preparedHttpLogFormatter.format(prepared);
    }

    @Override
    public String withBody(Correlation<HttpRequest, HttpResponse> response) throws IOException {
      return preparedHttpLogFormatter.format(response);
    }

    @Override
    public String withoutBody(Correlation<HttpRequest, HttpResponse> response) throws IOException {
      Map<String, Object> prepared = preparedHttpLogFormatter.prepare(response);
      if(prepared.containsKey("body")){
        prepared.put("body", REMOVED_BODY);
      }
      return preparedHttpLogFormatter.format(prepared);
    }
  }

  private class DefaultHttpMessageAsString implements HttpMessageAsString {

    private HttpLogFormatter logFormatter;

    public DefaultHttpMessageAsString(HttpLogFormatter logFormatter) {
      this.logFormatter = logFormatter;
    }

    @Override
    public String withBody(Precorrelation<HttpRequest> request) throws IOException {
      return logFormatter.format(request);
    }

    @Override
    public String withoutBody(Precorrelation<HttpRequest> request) throws IOException {
      String formatted = withBody(request);
      return formatted.replace(request.getRequest().getBodyAsString(), REMOVED_BODY);
    }

    @Override
    public String withBody(Correlation<HttpRequest, HttpResponse> response) throws IOException {
      return logFormatter.format(response);
    }

    @Override
    public String withoutBody(Correlation<HttpRequest, HttpResponse> response) throws IOException {
      String formatted = withBody(response);
      return formatted.replace(response.getResponse().getBodyAsString(), REMOVED_BODY);
    }
  }
}
