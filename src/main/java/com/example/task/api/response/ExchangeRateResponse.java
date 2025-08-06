package com.example.task.api.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Getter
public final class ExchangeRateResponse {

    private final String exchangeRatesDate;
    private final String from;
    private final String to;
    private final BigDecimal rate;

    @Builder
    public ExchangeRateResponse(LocalDate exchangeRatesDate, String from, String to, BigDecimal rate) {
        this.exchangeRatesDate = exchangeRatesDate.toString();
        this.from = from;
        this.to = to;
        this.rate = rate.setScale(2, RoundingMode.HALF_UP);
    }
}
