package com.example.task.infrastructure.repository;

import com.example.task.domain.CurrencyRate;
import com.example.task.domain.CurrencyRateTable;

import java.time.LocalDate;
import java.util.Optional;

public interface ExchangeRateRepository {

    void save(CurrencyRateTable currencyRateTable);

    boolean ratesAreActual(LocalDate date);

    Optional<CurrencyRate> findByCode(String code);

    boolean ratesExist();

    Optional<LocalDate> findLastRatesDate();

    void unlock();

    boolean lock();
}
