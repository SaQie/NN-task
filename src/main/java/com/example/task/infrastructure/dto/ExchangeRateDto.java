package com.example.task.infrastructure.dto;

import java.math.BigDecimal;

public record ExchangeRateDto(
        String currency,
        String code,
        BigDecimal bid,
        BigDecimal ask
) {}
