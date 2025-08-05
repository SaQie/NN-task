package com.example.task.infrastructure.provider;

import com.example.task.infrastructure.dto.ExchangeRateTableDto;

public interface ExchangeRateProvider {

    ExchangeRateTableDto fetchCurrentRates();


}
