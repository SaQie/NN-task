package com.example.task.infrastructure.scheduler;

import com.example.task.FakeExchangeRateProvider;
import com.example.task.FakeExchangeRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.catchException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExchangeRateSynchronizationSchedulerTest {

    private FakeExchangeRateRepository repository;
    private FakeExchangeRateProvider exchangeRateProvider;
    private ExchangeRateSynchronizationScheduler scheduler;

    @BeforeEach
    void setUp() {
        this.repository = new FakeExchangeRateRepository();
        this.exchangeRateProvider = FakeExchangeRateProvider.createWithActualDateWithRate();
        this.scheduler = new ExchangeRateSynchronizationScheduler(repository, exchangeRateProvider);
    }

    @Test
    @DisplayName("Should not make synchronization when database is locked")
    void test01() {
        // given
        repository.setLocked(false);

        // when
        scheduler.refreshExchangeRates();

        // then
        assertEquals(0, exchangeRateProvider.getFetchCurrentRatesCallCount());
    }

    @Test
    @DisplayName("Should make synchronization with lock when database is not locked")
    void test02() {
        // given
        repository.setLocked(true);

        // when
        scheduler.refreshExchangeRates();

        // then
        assertEquals(1, exchangeRateProvider.getFetchCurrentRatesCallCount());
        assertEquals(1, repository.getLockCallCount());
        assertEquals(1, repository.getUnlockCallCount());
    }

    @Test
    @DisplayName("Should remove lock even when exception occurred")
    void test03() {
        // given
        repository.setLocked(true);
        FakeExchangeRateProvider provider = FakeExchangeRateProvider.createWithException();
        ExchangeRateSynchronizationScheduler fakeScheduler = new ExchangeRateSynchronizationScheduler(repository, provider);

        // when
        Exception exception = catchException(fakeScheduler::refreshExchangeRates);

        // then
        assertNotNull(exception);
        assertEquals(1, provider.getFetchCurrentRatesCallCount());
        assertEquals(1, repository.getLockCallCount());
        assertEquals(1, repository.getUnlockCallCount());
    }

}