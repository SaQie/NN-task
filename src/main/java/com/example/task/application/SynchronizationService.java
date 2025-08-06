package com.example.task.application;

import com.example.task.domain.ExchangeRateSynchronizer;
import com.example.task.infrastructure.provider.ExchangeRateProvider;
import com.example.task.infrastructure.repository.ExchangeRateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SynchronizationService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final ExchangeRateProvider exchangeRateProvider;

    public void synchronizeManually(){
        ExchangeRateSynchronizer synchronizer = ExchangeRateSynchronizer.create(exchangeRateProvider, exchangeRateRepository);
        synchronizer.synchronizeIfNeeded();
    }
}
