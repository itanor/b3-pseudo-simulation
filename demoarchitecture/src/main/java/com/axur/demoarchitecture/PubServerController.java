package com.axur.demoarchitecture;

import java.math.BigDecimal;

import static java.util.UUID.randomUUID;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class PubServerController {

  private static final String AMOUNT_OF_CASH = "error.total.exceeds.amount.of.cash";
  
  private Total total;
  private PubService service;

  @PostMapping("/client-pub-server")
  public ResponseEntity<String> message(@RequestBody Message message) throws IOException {
    BigDecimal availableCash = total.getTotal();
    if (!message.totalLessOrEqualThanAvailable(availableCash)) {
      return new ResponseEntity<>(AMOUNT_OF_CASH, BAD_REQUEST);
    }
    message.setUuid(randomUUID());
    service.send(message);
    return new ResponseEntity<>("ok", OK);
  }
}
