package com.example.task.infrastructure.provider;

import com.example.task.infrastructure.dto.ExchangeRateTableDto;
import com.example.task.infrastructure.exception.ClientException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.util.retry.Retry;

import java.time.LocalDate;

@Component
class NbpApiExchangeRateProvider implements ExchangeRateProvider {

    private static final String RATES_URL = "exchangerates/tables/C?format=json";
    private static final String BASE_URL = "https://api.nbp.pl/api/";

    private final WebClient webClient;
    private final NbpApiRetryProperties retryProperties;

    NbpApiExchangeRateProvider(WebClient.Builder webClientBuilder, NbpApiRetryProperties retryProperties) {
        this.webClient = webClientBuilder
                .baseUrl(BASE_URL)
                .build();
        this.retryProperties = retryProperties;
    }

    @Override
    public ExchangeRateTableDto fetchCurrentRates() {
        ExchangeRateTableDto[] exchangeRateTableDto = webClient.get()
                .uri(RATES_URL)
                .retrieve()
                .bodyToMono(ExchangeRateTableDto[].class)
                .retryWhen(
                        Retry.backoff(
                                        retryProperties.getMaxRetries(),
                                        retryProperties.getMinBackoff())
                                .maxBackoff(retryProperties.getMaxBackoff())
                                .filter(throwable -> throwable instanceof WebClientRequestException)
                )
                .blockOptional()
                .orElseThrow(() -> new ClientException("There are no rates available in NBP API"));
        if (exchangeRateTableDto.length == 0) {
            throw new ClientException("There are no rates available in NBP API for (" + LocalDate.now() + ")");
        }
        return exchangeRateTableDto[0];
    }
}
