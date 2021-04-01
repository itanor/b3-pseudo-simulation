package com.axur.b3service.b3service;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.zeromq.SocketType.SUB;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zeromq.ZContext;
import org.zeromq.ZMQ.Socket;

@ExtendWith(MockitoExtension.class)
public class SubThreadTest {
 
  @Mock private ZContext context;
  @Mock private SumaryService sumary;

  private SubThread thread;
  
  @Test
  public void shouldAssertThatThreadConnectAndReceiveData() throws InterruptedException {
    String connection = "tcp://localhost:61616";
	String topic = "some-topic";

	Socket subscriber = mock(Socket.class);
	when(context.createSocket(SUB)).thenReturn(subscriber);

	Set<Message> messages = new HashSet<>();
	thread = new SubThread(context, messages , connection , topic, sumary);
	thread.start();
	TimeUnit.MILLISECONDS.sleep(100);
	thread.interrupt();

	verify(subscriber, times(1)).connect(connection);
	verify(subscriber, times(1)).subscribe(topic);
	verify(subscriber, atLeastOnce()).recvStr();
	
	verify(sumary, atLeastOnce()).calcAndSend(messages);

	verify(subscriber, times(1)).close();
	verify(context, times(1)).close();
  }
}
