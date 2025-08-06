package com.example.task.infrastructure.provider;

import com.example.task.infrastructure.dto.ExchangeRateTableDto;
import com.example.task.infrastructure.exception.ClientException;

public interface ExchangeRateProvider {

    ExchangeRateTableDto fetchCurrentRates() throws ClientException;


}
