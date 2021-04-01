package com.axur.b3service.b3service;

import static java.lang.String.format;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {

  private UUID uuid;
  private FinancialAsset asset;
  private BigDecimal value;
  private Integer allotment;
  private Paper paper;

  public enum FinancialAsset {
    STOCK,      // ações
    REAL_ESTATE // fiis 
  }

  public enum Paper {
    VALE3,
    WEGE3,
    ITSA3,
    BOVA11
  }

  public BigDecimal calculateTotal() {
    return new BigDecimal(allotment).multiply(value);
  }

  public boolean totalLessOrEqualThanAvailable(BigDecimal available) {
    BigDecimal total = calculateTotal();
    return total.compareTo(available) <= 0;
  }

  public String composeToSend(String json) {
    return format("%s %s", asset.name().toLowerCase(), json);
  }
}

