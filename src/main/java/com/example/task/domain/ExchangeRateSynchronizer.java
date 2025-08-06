package com.example.task.domain;

import com.example.task.application.exception.RatesNotExistException;
import com.example.task.infrastructure.dto.ExchangeRateTableDto;
import com.example.task.infrastructure.dto.mapper.Mapper;
import com.example.task.infrastructure.exception.ClientException;
import com.example.task.infrastructure.provider.ExchangeRateProvider;
import com.example.task.infrastructure.repository.ExchangeRateRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public final class ExchangeRateSynchronizer {

    private final ExchangeRateProvider exchangeRateProvider;
    private final ExchangeRateRepository repository;

    public static ExchangeRateSynchronizer create(ExchangeRateProvider exchangeRateProvider, ExchangeRateRepository repository) {
        return new ExchangeRateSynchronizer(exchangeRateProvider, repository);
    }

    public void synchronizeIfNeeded() {
        LocalDate today = LocalDate.now();

        if (ratesAreFresh(today)) {
            log.info("Exchange rates are up to date ({}), skipping synchronization.", today);
            return;
        }

        log.info("Exchange rates are stale ({}), starting synchronization...", today);

        try {
            ExchangeRateTableDto dto = exchangeRateProvider.fetchCurrentRates();
            repository.save(Mapper.toCurrencyRate(dto));
        } catch (ClientException e) {
            if (repository.ratesExist()) {
                log.error("Rates exist in cache, but could not fetch current rates! Adjusting current rates...", e);
                repository.adjustExistingRates();
                return;
            }
            throw new RatesNotExistException("Error fetching rates!", e);
        }

        log.info("Exchange rates updated successfully for {}.", today);
    }

    private boolean ratesAreFresh(LocalDate today) {
        return repository.ratesAreActual(today);
    }


}
