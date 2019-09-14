package com.alarmcontrol.server.logging;

import java.io.IOException;
import java.util.UUID;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class HttpRequestMDCIdFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {

    try {
      MDC.put(MDCKeys.REQUEST_ID, UUID.randomUUID().toString());
      filterChain.doFilter(request, response);
    }finally {
      MDC.clear();
    }
  }
}
