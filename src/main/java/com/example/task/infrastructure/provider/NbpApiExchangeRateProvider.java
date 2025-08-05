package com.example.task.infrastructure.provider;

import com.example.task.infrastructure.dto.ExchangeRateTableDto;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class NbpApiExchangeRateProvider implements ExchangeRateProvider {

    private static final String RATES_URL = "exchangerates/tables/C?format=json";
    private static final String BASE_URL = "https://api.nbp.pl/api/";

    private final WebClient webClient;

    public NbpApiExchangeRateProvider(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl(BASE_URL)
                .build();
    }

    @Override
    public ExchangeRateTableDto fetchCurrentRates() {
        return webClient.get()
                .uri(RATES_URL)
                .retrieve()
                .bodyToMono(ExchangeRateTableDto[].class)
                .map(tables -> {
                    if (tables.length == 0) {
                        throw new IllegalStateException("NBP API zwróciło pustą odpowiedź");
                    }
                    return tables[0];
                }).blockOptional()
                .orElseThrow(() -> new IllegalStateException("NBP API zwróciło null"));
    }
}
