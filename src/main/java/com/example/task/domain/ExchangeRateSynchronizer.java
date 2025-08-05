package com.example.task.domain;

import com.example.task.infrastructure.dto.ExchangeRateTableDto;
import com.example.task.infrastructure.dto.mapper.Mapper;
import com.example.task.infrastructure.provider.ExchangeRateProvider;
import com.example.task.infrastructure.repository.ExchangeRateRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class ExchangeRateSynchronizer {

    private final ExchangeRateProvider exchangeRateProvider;
    private final ExchangeRateRepository repository;

    public static ExchangeRateSynchronizer create(ExchangeRateProvider exchangeRateProvider, ExchangeRateRepository repository) {
        return new ExchangeRateSynchronizer(exchangeRateProvider, repository);
    }

    public void synchronizeIfNeeded() {
        if (areExchangeRatesUpToDate()) {
            log.info("Exchange rates are up to date ({})", LocalDate.now());
            return;
        }
        log.info("Exchange rates are not up to date, trying to fetch current rates for ({})", LocalDate.now());
        ExchangeRateTableDto dto = exchangeRateProvider.fetchCurrentRates();
        repository.save(Mapper.toCurrencyRate(dto));
        log.info("Exchange rates updated for ({})", LocalDate.now());
    }

    private boolean areExchangeRatesUpToDate() {
        LocalDate now = LocalDate.now();
        return repository.ratesAreActual(now);
    }


}
