package com.axur.b3service.b3service;

import static java.util.UUID.randomUUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class MessageTest {

  @Test
  public void shouldAssertTotalValue() {
    Message message = new Message(
      randomUUID(),
      Message.FinancialAsset.STOCK,
      new BigDecimal("50.71"),
      100,
      Message.Paper.WEGE3
    );
    assertThat(message.calculateTotal(), equalTo(new BigDecimal("5071.00")));
  }

  @Test
  public void shouldAssertTotalIsLessThanAvailable() {
    Message message = new Message(
      randomUUID(),
      Message.FinancialAsset.STOCK,
      new BigDecimal("80.83"),
      100,
      Message.Paper.WEGE3
    );
    BigDecimal available = new BigDecimal("10000.90");

    assertThat(message.totalLessOrEqualThanAvailable(available), equalTo(true));
  }
  
  @Test
  public void shouldAssertTotalIsEqualThanAvailable() {
    Message message = new Message(
      randomUUID(),
      Message.FinancialAsset.STOCK,
      new BigDecimal("80.83"),
      100,
      Message.Paper.WEGE3
    );
    BigDecimal available = new BigDecimal("8083.00");

    assertThat(message.totalLessOrEqualThanAvailable(available), equalTo(true));
  }

  @Test
  public void shouldAssertTotalIsBiggerThanAvailable() {
    Message message = new Message(
      randomUUID(),
      Message.FinancialAsset.STOCK,
      new BigDecimal("80.84"),
      100,
      Message.Paper.WEGE3
    );
    BigDecimal available = new BigDecimal("8083.00");

    assertThat(message.totalLessOrEqualThanAvailable(available), equalTo(false));
  }
}
