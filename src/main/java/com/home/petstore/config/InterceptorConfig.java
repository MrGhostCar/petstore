package com.home.petstore.config;

import com.home.petstore.interceptor.RequestMethodInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
  @Autowired RequestMethodInterceptor interceptor;

  @Override
  public void addInterceptors(final InterceptorRegistry registry) {
    registry
        .addInterceptor(interceptor)
        .addPathPatterns("/store/order/**");
  }
}
