package com.axur.demoarchitecture;

import static com.axur.demoarchitecture.Message.FinancialAsset.STOCK;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.zeromq.SocketType.REQ;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zeromq.ZContext;
import org.zeromq.ZMQ.Socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class PubServiceTest {

  @Mock private ZContext context;
  @Mock private ObjectMapper mapper;
  @Mock private ZmqConfig zmqConf;
  
  private PubService service;
  
  @BeforeEach
  public void beforeEach() {
    service = new PubService(context, mapper, zmqConf); 
  }

  @Test
  public void shouldAssertMethodInvocationInside_send() throws JsonProcessingException {
    Message message = new Message();
    message.setAsset(STOCK);

    String url = "tcp://localhost:77777";
	when(zmqConf.getUrl()).thenReturn(url);

    String json = "{}";
	when(mapper.writeValueAsString(message)).thenReturn(json);

	Socket reqSocket = mock(Socket.class);
	when(context.createSocket(REQ)).thenReturn(reqSocket);
	service.send(message);
	
	verify(mapper, times(1)).writeValueAsString(message);
	verify(reqSocket, times(1)).connect(zmqConf.getUrl());
	verify(reqSocket, times(1)).send(message.composeToSend(json));
	verify(reqSocket, times(1)).recvStr();
	verify(context, times(1)).destroySocket(reqSocket);
  }
}
