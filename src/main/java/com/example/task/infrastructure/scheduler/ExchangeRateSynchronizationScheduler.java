package com.example.task.infrastructure.scheduler;

import com.example.task.infrastructure.provider.ExchangeRateProvider;
import com.example.task.infrastructure.repository.ExchangeRateRepository;
import com.example.task.domain.ExchangeRateSynchronizer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class ExchangeRateSynchronizationScheduler {

    private final ExchangeRateRepository exchangeRateRepository;
    private final ExchangeRateProvider exchangeRateProvider;

    @Scheduled(cron = "${nbp.refresh.cron}", zone = "${nbp.refresh.zone}")
    public void refreshExchangeRates() {
        // ShedLock - wspomnij o tym
        log.info("Refreshing exchange rates...");

        ExchangeRateSynchronizer synchronizer = ExchangeRateSynchronizer.create(exchangeRateProvider, exchangeRateRepository);
        synchronizer.synchronizeIfNeeded();
    }

}
