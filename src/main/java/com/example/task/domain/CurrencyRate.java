package com.example.task.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.math.MathContext;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Accessors(fluent = true)
public class CurrencyRate {

    private final String currency;
    private final String code;
    private final BigDecimal bid;
    private final BigDecimal ask;

    @JsonCreator
    public static CurrencyRate create(
            @JsonProperty("currency") String currency,
            @JsonProperty("code") String code,
            @JsonProperty("bid") BigDecimal bid,
            @JsonProperty("ask") BigDecimal ask) {
        return new CurrencyRate(currency, code, bid, ask);
    }

    public BigDecimal convertTo(CurrencyRate targetCurrency) {
        BigDecimal sourceToPln = this.ask();
        BigDecimal plnToTarget = BigDecimal.ONE.divide(targetCurrency.bid(), MathContext.DECIMAL64);

        return sourceToPln.multiply(plnToTarget);
    }


}
