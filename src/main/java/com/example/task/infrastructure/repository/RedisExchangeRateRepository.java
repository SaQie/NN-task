package com.example.task.infrastructure.repository;

import com.example.task.domain.CurrencyRate;
import com.example.task.domain.CurrencyRateTable;
import com.example.task.domain.TTLCalculator;
import com.example.task.infrastructure.dto.ExchangeRateTableDto;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class RedisExchangeRateRepository implements ExchangeRateRepository {

    private final RedisTemplate<String, String> dateRedisTemplate;
    private final RedisTemplate<String, CurrencyRate> currencyRateRedisTemplate;

    @Override
    public void save(CurrencyRateTable table) {
        LocalDate date = table.date();
        Duration ttl = TTLCalculator.calculateTTL(date);
        // Co jesli bedzie taka sytuacja ze czesciowo sie uda tutaj zapis a wywali sie na ostatniej walucie
        // Co jesli nie uda nam sie przez np. 3 dni pobrac wartosci z API
        table.rates()
                .forEach(rate -> currencyRateRedisTemplate.opsForValue().set(rate.code(), rate));

        dateRedisTemplate.opsForValue().set(date.toString(), "1", ttl);
    }

    @Override
    public boolean ratesAreActual(LocalDate date) {
        LocalDate properDate = TTLCalculator.recognizeDateIfWeekend(date);
        return dateRedisTemplate.hasKey(properDate.toString());
    }

    @Override
    public Optional<CurrencyRate> findByCode(String code) {
        CurrencyRate rate = currencyRateRedisTemplate.opsForValue().get(code);
        return Optional.ofNullable(rate);
    }
}
