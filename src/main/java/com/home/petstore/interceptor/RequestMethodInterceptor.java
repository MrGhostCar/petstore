package com.home.petstore.interceptor;

import com.home.petstore.exception.ApiKeyException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestMethodInterceptor implements HandlerInterceptor {

  @Value("${x-api-key}")
  private String environmentLoadedApiKey;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    // We allow CORS checks from browsers
    if ("OPTIONS".equals(request.getMethod())) {
      return true;
    }

    // Get the 'Accept' header value
    String headerValue = request.getHeader("X-Api-Key");

    if (headerValue == null) {
      response.setStatus(401);
      return false;
    }

    // check the request contains expected header value
    if (!headerValue.equals(environmentLoadedApiKey)) {
      // Reject and Log or Ignore upon your requirement & return false
      response.setStatus(401);
      return false;
    } else {
      return true;
    }
  }
}
