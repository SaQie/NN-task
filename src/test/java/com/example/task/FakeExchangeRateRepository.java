package com.example.task;

import com.example.task.domain.CurrencyRate;
import com.example.task.domain.CurrencyRateTable;
import com.example.task.infrastructure.repository.ExchangeRateRepository;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class FakeExchangeRateRepository implements ExchangeRateRepository {

    private LocalDate date;
    private final Map<String, CurrencyRate> currencyRateMap = new HashMap<>();

    @Getter
    private int saveCallCount;
    @Getter
    private int ratesAreActualCallCount;
    @Getter
    private int adjustExistingRatesCallCount;
    @Getter
    private int ratesExistCallCount;

    @Override
    public void save(CurrencyRateTable currencyRateTable) {
        date = currencyRateTable.date();
        currencyRateTable.rates().forEach(rate -> currencyRateMap.put(rate.code(), rate));
        saveCallCount++;
    }

    @Override
    public boolean ratesAreActual(LocalDate date) {
        ratesAreActualCallCount++;
        return this.date != null && date.isEqual(this.date);
    }

    @Override
    public Optional<CurrencyRate> findByCode(String code) {
        return Optional.ofNullable(currencyRateMap.get(code));
    }

    @Override
    public void adjustExistingRates() {
        adjustExistingRatesCallCount++;
    }

    @Override
    public boolean ratesExist() {
        ratesExistCallCount++;
        return date != null;
    }

    @Override
    public LocalDate getRatesDate() {
        return date;
    }

    public void setActualRatesDate(LocalDate date) {
        this.date = date;
    }

    public void addCode(String code) {
        CurrencyRate currencyRate = CurrencyRate.create("currency", code, new BigDecimal("1.11"), new BigDecimal("1.11"));
        currencyRateMap.put(code, currencyRate);
    }


}
