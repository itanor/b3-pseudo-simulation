package com.axur.realestate;

import org.zeromq.ZMQ.Socket;

import static org.zeromq.SocketType.SUB;

import org.zeromq.ZContext;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@AllArgsConstructor
public class SubThread extends Thread {

  private ZContext context;
  private String connection;
  private String topic;

  @Override
  public void run() {
    Socket subscriber = context.createSocket(SUB);

    subscriber.connect(connection);
    subscriber.subscribe(topic);

    while (!Thread.currentThread().isInterrupted()) {
      String content = subscriber.recvStr();
      log.info("sub received: " + content);
    }
    subscriber.close();
    context.close();
  }
}
