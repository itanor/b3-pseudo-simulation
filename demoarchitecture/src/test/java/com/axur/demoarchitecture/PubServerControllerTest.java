package com.axur.demoarchitecture;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static com.axur.demoarchitecture.Message.FinancialAsset.STOCK;
import static com.axur.demoarchitecture.Message.Paper.WEGE3;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class PubServerControllerTest {

  @Mock private Total total;
  @Mock private PubService service;

  private PubServerController controller;

  @Test
  public void shouldAssertThatTotalOfMessageIsLessThanAvailableCash() throws IOException {
	Message message = new Message(null, STOCK, new BigDecimal("80.83"), 10, WEGE3);

	BigDecimal totalCash = new BigDecimal("1000000.90");
	when(total.getTotal()).thenReturn(totalCash);

    controller = new PubServerController(total, service);    
	ResponseEntity<String> response = controller.message(message);
	
	assertThat(message.getUuid(), notNullValue());
	verify(service, times(1)).send(message);
	
	assertThat(response, notNullValue());
	assertThat(response.getBody(), equalTo("ok"));
	assertThat(response.getStatusCode(), equalTo(OK));
  }

  @Test
  public void shouldAssertThatTotalOfMessageIsBiggerThanAvailableCash() throws IOException {
	Message message = new Message(null, STOCK, new BigDecimal("80.83"), 100000, WEGE3);

	BigDecimal totalCash = new BigDecimal("1000000.90");
	when(total.getTotal()).thenReturn(totalCash);

    controller = new PubServerController(total, service);    
	ResponseEntity<String> response = controller.message(message);
	
	assertThat(message.getUuid(), nullValue());
	verify(service, times(0)).send(message);

	assertThat(response, notNullValue());
	assertThat(response.getBody(), equalTo("error.total.exceeds.amount.of.cash"));
	assertThat(response.getStatusCode(), equalTo(BAD_REQUEST));
  }
}
