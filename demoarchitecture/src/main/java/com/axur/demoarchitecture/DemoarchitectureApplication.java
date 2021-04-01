package com.axur.demoarchitecture;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.zeromq.ZContext;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootApplication
public class DemoarchitectureApplication implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(DemoarchitectureApplication.class, args);
  }

  @Override
  public void run(String... args) {
    if (args != null && args.length > 0) {
      String topic = args[0];
      log.info("topic: " + topic);
      SubThread subscriber = new SubThread( 
        new ZContext(),
        "tcp://server-receive:20000",
        topic
      );
      subscriber.start();
      log.info("thread started...");
    }
  }
}
