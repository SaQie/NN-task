package com.example.task.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Accessors(fluent = true)
public final class CurrencyRateTable {

    private final LocalDate date;
    private final List<CurrencyRate> rates;

    public static CurrencyRateTable create(LocalDate date, List<CurrencyRate> rates) {
        rates.add(CurrencyRate.pln());
        return new CurrencyRateTable(date, rates);
    }

}
