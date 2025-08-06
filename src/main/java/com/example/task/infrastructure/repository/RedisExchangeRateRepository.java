package com.example.task.infrastructure.repository;

import com.example.task.application.exception.RatesNotExistException;
import com.example.task.domain.CurrencyRate;
import com.example.task.domain.CurrencyRateTable;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Repository
@AllArgsConstructor
class RedisExchangeRateRepository implements ExchangeRateRepository {

    private static final String DATE_PATTERN = "????-??-??";

    private final RedisTemplate<String, String> dateRedisTemplate;
    private final RedisTemplate<String, CurrencyRate> currencyRateRedisTemplate;
    private final TTLCalculator ttlCalculator = TTLCalculator.create();

    @Override
    public void save(CurrencyRateTable table) {
        // Transakcje na redis - wspomnij o tym
        LocalDate date = table.date();
        Duration ttl = ttlCalculator.calculateTTL(date);
        table.rates()
                .forEach(rate -> currencyRateRedisTemplate.opsForValue().set(rate.code(), rate));

        dateRedisTemplate.opsForValue().set(date.toString(), "1", ttl);
    }

    @Override
    public boolean ratesAreActual(LocalDate date) {
        LocalDate properDate = ttlCalculator.recognizeDateIfWeekend(date);
        return dateRedisTemplate.hasKey(properDate.toString());
    }

    @Override
    public Optional<CurrencyRate> findByCode(String code) {
        CurrencyRate rate = currencyRateRedisTemplate.opsForValue().get(code);
        return Optional.ofNullable(rate);
    }

    @Override
    public void adjustExistingRates() {
        Set<String> keys = dateRedisTemplate.keys(DATE_PATTERN);
        String redisLastRatesDateRaw = keys.iterator().next();
        LocalDate redisLastRatesDate = LocalDate.parse(redisLastRatesDateRaw);
        Long currentTTL = dateRedisTemplate.getExpire(redisLastRatesDateRaw, TimeUnit.SECONDS);
        Duration calculatedTTL = ttlCalculator.calculateTTL(redisLastRatesDate);
        long newTtl = currentTTL + calculatedTTL.getSeconds();
        dateRedisTemplate.expire(redisLastRatesDateRaw, newTtl, TimeUnit.SECONDS);
    }

    @Override
    public boolean ratesExist() {
        Set<String> matchingKeys = dateRedisTemplate.keys(DATE_PATTERN);
        return !matchingKeys.isEmpty();
    }

    @Override
    public LocalDate getRatesDate() {
        if (!ratesExist()) {
            throw new RatesNotExistException("Rates not exist");
        }
        Set<String> keys = dateRedisTemplate.keys(DATE_PATTERN);
        String redisLastRatesDateRaw = keys.iterator().next();
        return LocalDate.parse(redisLastRatesDateRaw);
    }
}
