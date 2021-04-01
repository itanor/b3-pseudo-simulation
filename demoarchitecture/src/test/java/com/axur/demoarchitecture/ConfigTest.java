package com.axur.demoarchitecture;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zeromq.ZContext;

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
    
    Method total = clazz.getDeclaredMethod("total");
    assertThat(total, notNullValue());

    Bean totalBean = total.getAnnotation(Bean.class);
    assertThat(totalBean, notNullValue());
  }
  
  @Test
  public void shouldAssertReturnMethodValues() {
    Config config = new Config();
    ZContext zmqContext = config.zmqContext();
    
	assertThat(zmqContext, notNullValue());
    assertThat(zmqContext, instanceOf(ZContext.class));
    
    Total total = config.total();
    assertThat(total, notNullValue());
    assertThat(total, instanceOf(Total.class));
  }
}
