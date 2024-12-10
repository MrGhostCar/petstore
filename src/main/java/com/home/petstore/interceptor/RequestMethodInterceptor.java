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
    private String bar;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        //Get the 'Accept' header value
        String headerValue = request.getHeader("x-api-key");

        if (headerValue == null)
            throw new ApiKeyException("Api key missing.");

        //check the request contains expected header value
        if(!headerValue.equals(bar)) {
            //Reject and Log or Ignore upon your requirement & return false
            return false;
        } else {
            return true;
        }
    }
}
