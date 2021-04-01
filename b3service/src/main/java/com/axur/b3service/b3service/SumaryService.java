package com.axur.b3service.b3service;

import static java.lang.String.format;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static org.zeromq.SocketType.REQ;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Function;
import java.util.Collection;
import java.util.Set;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.zeromq.ZContext;
import org.zeromq.ZMQ.Socket;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Service;

@Log4j2
@Service
@AllArgsConstructor
public class SumaryService {

  private ZContext context;
  private ObjectMapper mapper;
  private ZmqConfig zmqConf;

  public Message convertToMessage(String content) {
    int beginOfJson = content.indexOf("{");
    String json = content.substring(beginOfJson, content.length());

    Message message = null;
    try {
      message = mapper.readValue(json, Message.class);
    } catch(JsonProcessingException e) {
      e.printStackTrace();
    }
    return message;
  }

  public void calcAndSend(Set<Message> messages) {
    String json = "";

    Collection<Sumary> sumaries = doSumary(messages);
    try {
      json = mapper.writeValueAsString(sumaries);
    } catch(JsonProcessingException e) {
      e.printStackTrace();
    }

    Socket req = context.createSocket(REQ);
    req.connect(zmqConf.getUrl());

    String toSend = format("%s %s", zmqConf.getTopicToSendBack(), json); 
    req.send(toSend);
    log.info("sent: " + toSend);
    String received = req.recvStr();
    log.info("received: " + received);

    context.destroySocket(req);
  }

  public Collection<Sumary> doSumary(Set<Message> messages) {
    Set<Sumary> sumaries = messages.stream()
      .map(message ->
        new Sumary(
          message.getPaper(), 
		  BigInteger.valueOf(message.getAllotment()), 
          message.getValue().multiply(new BigDecimal(message.getAllotment()))
        )
      )
      .collect(toSet());

    return sumaries.stream()
      .collect(
        toMap(Sumary::getPaper,
        Function.identity(),
        Sumary::merge))
      .values();
	}
}

