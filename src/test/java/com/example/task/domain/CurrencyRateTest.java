package com.example.task.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyRateTest {

    @Test
    @DisplayName("Should convert works correctly")
    void test01() {
        // given: 1 EUR = 4.50 PLN (ask), 1 USD = 3.00 PLN (bid)
        CurrencyRate eurRate = CurrencyRate.create("Euro", "EUR", null, new BigDecimal("4.50"));
        CurrencyRate usdRate = CurrencyRate.create("Dollar", "USD", new BigDecimal("3.00"), null);

        // when: converting 1 EUR to USD
        BigDecimal result = eurRate.convertTo(usdRate);

        // then: should be 4.50 PLN / 3.00 PLN = 1.50 USD
        assertEquals(new BigDecimal("1.50"), result.setScale(2, RoundingMode.HALF_UP));
    }


    @Test
    @DisplayName("Should initialize all fields correctly")
    void test02() {
        // given
        String currency = "TestCurrency";
        String code = "TST";
        BigDecimal bid = new BigDecimal("2.25");
        BigDecimal ask = new BigDecimal("2.30");

        // when
        CurrencyRate rate = CurrencyRate.create(currency, code, bid, ask);

        // then
        assertAll(
                () -> assertEquals(currency, rate.currency()),
                () -> assertEquals(code, rate.code()),
                () -> assertEquals(bid, rate.bid()),
                () -> assertEquals(ask, rate.ask())
        );
    }
}