package com.axur.demoarchitecture;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.zeromq.ZContext;
import java.math.BigDecimal;

@Configuration
public class Config {

  @Bean
  public ZContext zmqContext() {
    return new ZContext();
  }

  @Bean
  public Total total() {
    return new Total(new BigDecimal("125000.96"));
  }
}
