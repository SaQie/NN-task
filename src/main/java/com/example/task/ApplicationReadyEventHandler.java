package com.example.task;

import com.example.task.infrastructure.provider.ExchangeRateProvider;
import com.example.task.domain.ExchangeRateSynchronizer;
import com.example.task.infrastructure.repository.ExchangeRateRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
@AllArgsConstructor
@Slf4j
public class ApplicationReadyEventHandler implements ApplicationListener<ApplicationReadyEvent> {

    private final ExchangeRateProvider exchangeRateProvider;
    private final ExchangeRateRepository repository;

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        boolean active = TransactionSynchronizationManager.isActualTransactionActive();
        System.out.println(active);
        ExchangeRateSynchronizer initializer = ExchangeRateSynchronizer.create(exchangeRateProvider, repository);
        initializer.synchronizeIfNeeded();
    }
}
