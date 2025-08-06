package com.example.task.domain;

import com.example.task.FakeExchangeRateProvider;
import com.example.task.FakeExchangeRateRepository;
import com.example.task.application.exception.RatesNotExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.catchException;
import static org.junit.jupiter.api.Assertions.*;

class ExchangeRateSynchronizerTest {

    private FakeExchangeRateRepository repository;

    @BeforeEach
    void setUp() {
        this.repository = new FakeExchangeRateRepository();
    }

    @Test
    @DisplayName("Should not process synchronization when rates are actual")
    void test01() {
        // given
        repository.setActualRatesDate(LocalDate.now());
        FakeExchangeRateProvider rateProvider = FakeExchangeRateProvider.createWithActualDateWithRate();
        ExchangeRateSynchronizer synchronizer = ExchangeRateSynchronizer.create(rateProvider, repository);

        // when
        synchronizer.synchronizeIfNeeded();

        // then
        assertEquals(0, rateProvider.getFetchCurrentRatesCallCount());
    }

    @Test
    @DisplayName("Should update current rates when exchange rates are stale")
    void test02() {
        // given
        repository.setActualRatesDate(LocalDate.now().minusDays(1));
        FakeExchangeRateProvider rateProvider = FakeExchangeRateProvider.createWithActualDateWithRate();
        ExchangeRateSynchronizer synchronizer = ExchangeRateSynchronizer.create(rateProvider, repository);

        // when
        synchronizer.synchronizeIfNeeded();

        // then
        assertEquals(1, rateProvider.getFetchCurrentRatesCallCount());
        assertEquals(1, repository.getSaveCallCount());
        assertEquals(LocalDate.now(), repository.getRatesDate());
        assertTrue(repository.ratesExist());
    }

    @Test
    @DisplayName("Should throw RatesNotExist exception when cache doesn't have exchange rates and something went wrong while fetching rates from API")
    void test03() {
        // given
        FakeExchangeRateProvider rateProvider = FakeExchangeRateProvider.createWithException();
        ExchangeRateSynchronizer synchronizer = ExchangeRateSynchronizer.create(rateProvider, repository);

        // when
        Exception exception = catchException(synchronizer::synchronizeIfNeeded);

        // then
        assertInstanceOf(RatesNotExistException.class, exception);
    }

    @Test
    @DisplayName("Should call adjust existing rates when something went wrong while fetching data from API but cache contains previous day exchange rates")
    void test04(){
        // given
        repository.setActualRatesDate(LocalDate.now().minusDays(1));
        FakeExchangeRateProvider rateProvider = FakeExchangeRateProvider.createWithException();
        ExchangeRateSynchronizer synchronizer = ExchangeRateSynchronizer.create(rateProvider, repository);

        // when
        synchronizer.synchronizeIfNeeded();

        // then
        assertEquals(1, rateProvider.getFetchCurrentRatesCallCount());
        assertEquals(0, repository.getSaveCallCount());
        assertEquals(1, repository.getAdjustExistingRatesCallCount());
        assertEquals(LocalDate.now().minusDays(1), repository.getRatesDate());
        assertTrue(repository.ratesExist());
    }
}