package com.axur.b3service.b3service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.HashSet;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.zeromq.ZContext;

@Log4j2
@AllArgsConstructor
@SpringBootApplication
public class B3serviceApplication implements CommandLineRunner {

  private SumaryService sumary;

  public static void main(String[] args) {
    SpringApplication.run(B3serviceApplication.class, args);
  }

  @Override
  public void run(String... args) {
    if (args != null && args.length > 0) {
      String topic = args[0];
      log.info("topic: " + topic);

      SubThread subscriber = new SubThread(
	    new ZContext(),
		new HashSet<Message>(), 
		"tcp://server-send:20000", 
		topic,
		sumary
      );
      subscriber.start();
      log.info("thread started...");
    }
  }
}
