package com.example.task.application;

import com.example.task.api.query.ExchangeRateQuery;
import com.example.task.api.response.ExchangeRateResponse;
import com.example.task.application.exception.CodeNotFoundException;
import com.example.task.application.exception.RatesNotExistException;
import com.example.task.domain.CurrencyRate;
import com.example.task.infrastructure.repository.ExchangeRateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;

    public ExchangeRateResponse process(ExchangeRateQuery query) {
        checkIfRatesExist();

        CurrencyRate from = getCurrencyRate(query.from());
        CurrencyRate to = getCurrencyRate(query.to());
        LocalDate ratesDate = getRatesDate();

        BigDecimal rate = from.convertTo(to);

        return ExchangeRateResponse.builder()
                .exchangeRatesDate(ratesDate)
                .rate(rate)
                .from(from.code())
                .to(to.code())
                .build();
    }

    private LocalDate getRatesDate() {
        return exchangeRateRepository.getRatesDate();
    }

    private CurrencyRate getCurrencyRate(String code) {
        return exchangeRateRepository.findByCode(code)
                .orElseThrow(() -> new CodeNotFoundException("Code " + code + " not found"));
    }

    private void checkIfRatesExist() {
        boolean ratesExist = exchangeRateRepository.ratesExist();
        if (!ratesExist) {
            throw new RatesNotExistException("Rates Not Exist");
        }
    }

}
