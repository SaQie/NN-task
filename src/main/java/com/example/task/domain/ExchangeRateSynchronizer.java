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
            fetchAndSaveCurrentRates();
            log.info("Exchange rates updated successfully for {}.", today);
        } catch (Exception e) {
            log.error("Critical failure fetching rates and no cached data available!", e);
            throw e;
        }
    }

    private boolean ratesAreFresh(LocalDate today) {
        return repository.ratesAreActual(today);
    }

    private void fetchAndSaveCurrentRates() {
        try {
            ExchangeRateTableDto dto = exchangeRateProvider.fetchCurrentRates();
            repository.save(Mapper.toCurrencyRate(dto));
        } catch (Exception e) {
            if (repository.ratesExist()) {
                repository.adjustExistingRates();
                log.error("Rates exist but could not fetch current rates!", e);
            }
            throw e;
        }
    }


}
