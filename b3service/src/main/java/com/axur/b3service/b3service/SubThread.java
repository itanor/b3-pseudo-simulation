package com.axur.b3service.b3service;

import static org.zeromq.SocketType.SUB;

import java.util.Set;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SubThread extends Thread {

  private ZContext context;
  private Set<Message> messages;
  private String connection;
  private String topic;
  private SumaryService sumary;

  @Override
  public void run() {
    Socket subscriber = context.createSocket(SUB);

    subscriber.connect(connection);
    subscriber.subscribe(topic);
 
    int messagesReceived = 0;
    while (!Thread.currentThread().isInterrupted()) {
      String content = subscriber.recvStr();
      System.out.println("sub received: " + content);
      messagesReceived++;

      Message message = sumary.convertToMessage(content);
      messages.add(message);
      if (messagesReceived % 3 == 0) {
        sumary.calcAndSend(messages);
      }
    }
    subscriber.close();
    context.close();
  }
}
