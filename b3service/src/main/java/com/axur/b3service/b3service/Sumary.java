package com.axur.b3service.b3service;

import java.math.BigInteger;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Sumary { 

  private Message.Paper paper;
  private BigInteger totalAllotment;
  private BigDecimal totalValue;

  public static Sumary merge(Sumary first, Sumary second) {
    first.setTotalAllotment(first.getTotalAllotment().add(second.getTotalAllotment()));
    first.setTotalValue(first.getTotalValue().add(second.getTotalValue()));
    return first;
  }
}
