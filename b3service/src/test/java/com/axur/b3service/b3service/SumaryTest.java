package com.axur.b3service.b3service;

import static com.axur.b3service.b3service.Sumary.merge;

import java.math.BigDecimal;
import java.math.BigInteger;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.Test;

public class SumaryTest {

  @Test
  public void shouldAssertMergeOfTwoSumaries() {
    Sumary first = new Sumary(null, new BigInteger("45"), new BigDecimal("10000.90"));
	Sumary second = new Sumary(null, new BigInteger("50"), new BigDecimal("5000.70"));

	merge(first, second);
	
	assertThat(first.getTotalAllotment(), equalTo(new BigInteger("95")));
	assertThat(first.getTotalValue(), equalTo(new BigDecimal("15001.60")));
  }
}
