package com.example.task.application;

import com.example.task.api.query.ExchangeRateQuery;
import com.example.task.api.response.ExchangeRateResponse;
import com.example.task.domain.CurrencyRate;
import com.example.task.infrastructure.repository.ExchangeRateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;

    public ExchangeRateResponse process(ExchangeRateQuery query) {
        CurrencyRate from = exchangeRateRepository.findByCode(query.from())
                .orElseThrow(() -> new IllegalStateException("Code " + query.from() + " not found"));
        CurrencyRate to = exchangeRateRepository.findByCode(query.to())
                .orElseThrow(() -> new IllegalStateException("Code " + query.to() + " not found"));

        BigDecimal rate = from.convertTo(to);

        return ExchangeRateResponse.builder()
                .rate(rate)
                .from(from.code())
                .to(to.code())
                .build();
    }

}
