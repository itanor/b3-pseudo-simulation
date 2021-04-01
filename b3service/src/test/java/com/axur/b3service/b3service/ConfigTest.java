package com.axur.b3service.b3service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class ConfigTest {

  @Test
  public void shouldAssertClassMetadata() throws NoSuchMethodException, SecurityException {
    Class<? extends Config> clazz = new Config().getClass();
    Configuration configuration = clazz.getAnnotation(Configuration.class);
    
    assertThat(configuration, notNullValue());
    
    Method zmqContext = clazz.getDeclaredMethod("zmqContext");
    assertThat(zmqContext, notNullValue());

    Bean zmqBean = zmqContext.getAnnotation(Bean.class);
    assertThat(zmqBean, notNullValue());
  }
}
