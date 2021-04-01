package com.axur.b3service.b3service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

public class ZmqConfigTest {

  @Test
  public void shouldAssertClassMetadata() {
    Class<? extends ZmqConfig> clazz = new ZmqConfig().getClass();
    ConfigurationProperties properties = clazz.getAnnotation(ConfigurationProperties.class);

    assertThat(properties.prefix(), equalTo("zmq.server"));
    
    Configuration configuration = clazz.getAnnotation(Configuration.class);
    assertThat(configuration, notNullValue());
  }
}
