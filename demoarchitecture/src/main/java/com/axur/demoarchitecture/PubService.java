package com.axur.demoarchitecture;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import static org.zeromq.SocketType.REQ;

import org.springframework.stereotype.Service;

@Log4j2
@Service
@AllArgsConstructor
public class PubService {

  private ZContext context;
  private ObjectMapper mapper;
  private ZmqConfig zmqConf;

  public void send(Message message) throws JsonProcessingException {
    String json = mapper.writeValueAsString(message);

    ZMQ.Socket req = context.createSocket(REQ);
    req.connect(zmqConf.getUrl());

    String toSend = message.composeToSend(json); 
    log.info("sent: " + toSend);
    
    req.send(toSend);
    String received = req.recvStr();
    log.info("received: " + received);

    context.destroySocket(req);
  }
}

