package com.example.task;

import com.example.task.infrastructure.dto.ExchangeRateDto;
import com.example.task.infrastructure.dto.ExchangeRateTableDto;
import com.example.task.infrastructure.exception.ClientException;
import com.example.task.infrastructure.provider.ExchangeRateProvider;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class FakeExchangeRateProvider implements ExchangeRateProvider {

    private final boolean throwException;
    private final ExchangeRateTableDto exchangeRateTableDto;

    @Getter
    private int fetchCurrentRatesCallCount;

    @Override
    public ExchangeRateTableDto fetchCurrentRates() throws ClientException {
        fetchCurrentRatesCallCount++;
        if (throwException) {
            throw new ClientException("test");
        }
        return exchangeRateTableDto;
    }

    private static FakeExchangeRateProvider create(ExchangeRateTableDto exchangeRateTableDto) {
        return new FakeExchangeRateProvider(false, exchangeRateTableDto);
    }

    public static FakeExchangeRateProvider createWithActualDateWithoutRates() {
        ExchangeRateTableDto dto = new ExchangeRateTableDto(LocalDate.now(), Collections.emptyList());
        return new FakeExchangeRateProvider(false, dto);
    }

    public static FakeExchangeRateProvider createWithActualDateWithRate() {
        ExchangeRateDto euroRateDto = new ExchangeRateDto("Euro", "EUR", new BigDecimal("1.15"), new BigDecimal("1.14"));
        ExchangeRateDto usdRateDto = new ExchangeRateDto("Dolar amerykanski", "USD", new BigDecimal("1.70"), new BigDecimal("1.42"));
        ExchangeRateTableDto dto = new ExchangeRateTableDto(LocalDate.now(), List.of(usdRateDto, euroRateDto));
        return new FakeExchangeRateProvider(false, dto);
    }

    public static FakeExchangeRateProvider createWithPreviousDayWithRate() {
        ExchangeRateDto euroRateDto = new ExchangeRateDto("Euro", "EUR", new BigDecimal("1.15"), new BigDecimal("1.14"));
        ExchangeRateDto usdRateDto = new ExchangeRateDto("Dolar amerykanski", "USD", new BigDecimal("1.70"), new BigDecimal("1.42"));
        ExchangeRateTableDto dto = new ExchangeRateTableDto(LocalDate.now().minusDays(1), List.of(usdRateDto, euroRateDto));
        return new FakeExchangeRateProvider(false, dto);
    }

    public static FakeExchangeRateProvider createWithMondayDateWithRate() {
        ExchangeRateDto euroRateDto = new ExchangeRateDto("Euro", "EUR", new BigDecimal("1.15"), new BigDecimal("1.14"));
        ExchangeRateDto usdRateDto = new ExchangeRateDto("Dolar amerykanski", "USD", new BigDecimal("1.70"), new BigDecimal("1.42"));

        LocalDate mondayDate = LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        ExchangeRateTableDto dto = new ExchangeRateTableDto(mondayDate, List.of(usdRateDto, euroRateDto));
        return new FakeExchangeRateProvider(false, dto);
    }

    public static FakeExchangeRateProvider createWithSaturdayDateWithoutRates() {
        LocalDate weekendDate = LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.SATURDAY));
        ExchangeRateTableDto dto = new ExchangeRateTableDto(weekendDate, Collections.emptyList());
        return new FakeExchangeRateProvider(false, dto);
    }

    public static FakeExchangeRateProvider createWithSundayDateWithoutRates() {
        LocalDate weekendDate = LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        ExchangeRateTableDto dto = new ExchangeRateTableDto(weekendDate, Collections.emptyList());
        return new FakeExchangeRateProvider(false, dto);
    }

    public static FakeExchangeRateProvider createWithException() {
        return new FakeExchangeRateProvider(true, null);
    }


}
