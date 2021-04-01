package com.axur.b3service.b3service;

import static com.axur.b3service.b3service.Message.Paper.ITSA3;
import static com.axur.b3service.b3service.Message.Paper.WEGE3;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.zeromq.SocketType.REQ;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zeromq.ZContext;
import org.zeromq.ZMQ.Socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class SumaryServiceTest {

  @Mock private ZContext context;
  @Mock private ObjectMapper mapper;
  @Mock private ZmqConfig zmqConf;
  
  private SumaryService service;

  @BeforeEach
  public void beforeEach() {
    service = new SumaryService(context, mapper, zmqConf);
  }

  @Test
  public void shouldConvertContentToMessage() throws JsonMappingException, JsonProcessingException {
	String content = "stock {\"value\":80.83}";

	Message converted = new Message(null, null, new BigDecimal("80.83"), null, null);
	when(mapper.readValue("{\"value\":80.83}", Message.class)).thenReturn(converted);
	Message message = service.convertToMessage(content);

	assertThat(message, notNullValue());
	assertThat(message, equalTo(converted));
  }

  @Test
  public void shouldAssertDoSumary() {
    Set<Message> messages = new HashSet<>();
    messages.add(new Message(null, null, new BigDecimal("80.83"), 10, WEGE3));
    messages.add(new Message(null, null, new BigDecimal("45.56"), 10, ITSA3));
    messages.add(new Message(null, null, new BigDecimal("81.30"), 5, WEGE3));

	Collection<Sumary> sumary = service.doSumary(messages);
	assertThat(sumary, hasSize(2));
	
	sumary.forEach(s -> {
      assertThat(s.getPaper(), anyOf(equalTo(WEGE3), equalTo(ITSA3)));
      assertThat(s.getTotalAllotment(), 
    	anyOf(equalTo(BigInteger.valueOf(15)), equalTo(BigInteger.valueOf(10))));
      assertThat(s.getTotalValue(), 
        anyOf(equalTo(new BigDecimal("1214.80")), equalTo(new BigDecimal("455.60"))));
	});
  }

  @Test
  public void shouldAssertCalcAndSend() throws JsonProcessingException {
    Set<Message> messages = new HashSet<>();
    messages.add(new Message(null, null, new BigDecimal("80.83"), 10, WEGE3));
    messages.add(new Message(null, null, new BigDecimal("45.56"), 10, ITSA3));
    messages.add(new Message(null, null, new BigDecimal("81.30"), 5, WEGE3));

    String json = "{}";
	when(mapper.writeValueAsString(Mockito.anyCollectionOf(Sumary.class))).thenReturn(json);
	
	Socket socket = mock(Socket.class);
	when(context.createSocket(REQ)).thenReturn(socket);
	
	String url = "tpc://localhost:12121";
	when(zmqConf.getUrl()).thenReturn(url);
	String topic = "stock";
	when(zmqConf.getTopicToSendBack()).thenReturn(topic);
	
	service.calcAndSend(messages);
	
	verify(context, times(1)).createSocket(REQ);
	verify(socket, times(1)).connect(url);
	verify(socket, times(1)).send(topic + " " + json);
	verify(socket, times(1)).recvStr();
	verify(context, times(1)).destroySocket(socket);
  }
}
