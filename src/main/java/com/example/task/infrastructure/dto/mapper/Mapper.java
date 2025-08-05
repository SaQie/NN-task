package com.example.task.infrastructure.dto.mapper;

import com.example.task.domain.CurrencyRate;
import com.example.task.domain.CurrencyRateTable;
import com.example.task.infrastructure.dto.ExchangeRateTableDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Mapper {

    public static CurrencyRateTable toCurrencyRate(ExchangeRateTableDto tableDto) {
        List<CurrencyRate> currencyRates = tableDto.rates()
                .stream()
                .map(dto -> CurrencyRate.create(dto.currency(), dto.code(), dto.bid(), dto.ask())).toList();
        return CurrencyRateTable.create(tableDto.effectiveDate(), currencyRates);
    }

}
