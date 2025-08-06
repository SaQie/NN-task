package com.example.task.application;

import com.example.task.FakeExchangeRateProvider;
import com.example.task.FakeExchangeRateRepository;
import com.example.task.api.query.ExchangeRateQuery;
import com.example.task.api.response.ExchangeRateResponse;
import com.example.task.application.exception.CodeNotFoundException;
import com.example.task.application.exception.RatesNotExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.catchException;
import static org.junit.jupiter.api.Assertions.*;

class ExchangeRateServiceTest {

    private FakeExchangeRateRepository repository;
    private ExchangeRateService service;

    @BeforeEach
    void setUp() {
        repository = new FakeExchangeRateRepository();
        service = new ExchangeRateService(repository);
    }


    @Test
    @DisplayName("Should throw exception when rates not exist")
    void test01() {
        // given
        ExchangeRateQuery query = new ExchangeRateQuery("EUR", "USD");

        // when
        Exception exception = catchException(() -> service.process(query));

        // then
        assertInstanceOf(RatesNotExistException.class, exception);
    }

    @Test
    @DisplayName("Should throw exception when provided from code not exists")
    void test02() {
        // given
        repository.setActualRatesDate(LocalDate.now());
        repository.addCode("USD");
        ExchangeRateQuery query = new ExchangeRateQuery("EUR", "USD");

        // when
        Exception exception = catchException(() -> service.process(query));

        // then
        assertInstanceOf(CodeNotFoundException.class, exception);
        assertEquals("Code EUR not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when provided to code not exist")
    void test03() {
        // given
        repository.setActualRatesDate(LocalDate.now());
        repository.addCode("EUR");
        ExchangeRateQuery query = new ExchangeRateQuery("EUR", "USD");

        // when
        Exception exception = catchException(() -> service.process(query));

        // then
        assertInstanceOf(CodeNotFoundException.class, exception);
        assertEquals("Code USD not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should correctly convert from to to code and return response")
    void test04() {
        // given
        repository.setActualRatesDate(LocalDate.now());
        repository.addCode("EUR");
        repository.addCode("USD");
        ExchangeRateQuery query = new ExchangeRateQuery("EUR", "USD");

        // when
        ExchangeRateResponse response = service.process(query);

        // then
        assertNotNull(response);
        assertNotNull(response.getRate());
        assertEquals(LocalDate.now().toString(), response.getExchangeRatesDate());
        assertEquals("EUR", response.getFrom());
        assertEquals("USD", response.getTo());
    }

}