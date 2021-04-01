package com.axur.b3service.b3service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "zmq.server")
public class ZmqConfig {

  private String url;
  private String topicToSendBack;
}

