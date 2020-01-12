package com.alarmcontrol.server.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.listener.RetryListenerSupport;

public class DefaultRetryListenerSupport extends RetryListenerSupport {
  private Logger logger = LoggerFactory.getLogger(DefaultRetryListenerSupport.class);

  @Override
  public <T, E extends Throwable> boolean open(RetryContext context,
      RetryCallback<T, E> callback) {
    logger.trace("Starting retryable method");
    return super.open(context, callback);
  }

  @Override
  public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
    logger.trace("Finished retryable method");
    super.close(context, callback, throwable);
  }

  @Override
  public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
    logger.warn("Retryable method {} threw {}th exception {}",
        context.getAttribute("context.name"), context.getRetryCount(), throwable.getMessage(), throwable);
    super.onError(context, callback, throwable);
  }
}