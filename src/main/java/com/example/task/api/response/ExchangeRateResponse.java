package com.example.task.api.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
public final class ExchangeRateResponse {

    private final String from;
    private final String to;
    private final BigDecimal rate;

    @Builder
    public ExchangeRateResponse(String from, String to, BigDecimal rate) {
        this.from = from;
        this.to = to;
        this.rate = rate.setScale(2, RoundingMode.HALF_UP);
    }
}
