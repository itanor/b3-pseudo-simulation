package com.axur.b3service.b3service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.zeromq.ZContext;

@Configuration
public class Config {

  @Bean
  public ZContext zmqContext() {
    return new ZContext();
  }
}

