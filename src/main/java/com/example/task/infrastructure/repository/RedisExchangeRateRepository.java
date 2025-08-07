package com.example.task.infrastructure.repository;

import com.example.task.domain.CurrencyRate;
import com.example.task.domain.CurrencyRateTable;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Repository
@AllArgsConstructor
class RedisExchangeRateRepository implements ExchangeRateRepository {

    private static final String DATE_PATTERN = "????-??-??";
    private static final String LOCK_KEY = "lock:synchronization:";
    private static final long LOCK_TTL_SECONDS = 3600; // 1h


    private final RedisTemplate<String, Object> redisTemplate;
    private final DateCalculator dateCalculator = DateCalculator.create();

    @Override
    public void save(CurrencyRateTable table) {
        executeInTransaction(table);
    }

    private void executeInTransaction(CurrencyRateTable table) {
        Optional<LocalDate> dateOptional = findLastRatesDate();
        redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            @SuppressWarnings("unchecked")
            public <K, V> List<Object> execute(RedisOperations<K, V> operations) throws DataAccessException {
                RedisOperations<String, Object> ops = (RedisOperations<String, Object>) operations;
                ops.multi();

                ValueOperations<String, Object> vo = ops.opsForValue();
                for (CurrencyRate rate : table.rates()) {
                    vo.set(rate.code(), rate);
                }

                String dateKey = table.date().toString();
                vo.set(dateKey, "1");

                dateOptional.ifPresent(date -> ops.delete(dateKey));

                return ops.exec();
            }
        });
    }

    @Override
    public boolean ratesAreActual(LocalDate date) {
        LocalDate properDate = dateCalculator.recognizeDateIfWeekend(date);
        return redisTemplate.hasKey(properDate.toString());
    }

    @Override
    public Optional<CurrencyRate> findByCode(String code) {
        CurrencyRate rate = (CurrencyRate) redisTemplate.opsForValue().get(code);
        return Optional.ofNullable(rate);
    }

    @Override
    public boolean ratesExist() {
        Set<String> matchingKeys = redisTemplate.keys(DATE_PATTERN);
        return !matchingKeys.isEmpty();
    }

    @Override
    public Optional<LocalDate> findLastRatesDate() {
        Set<String> keys = redisTemplate.keys(DATE_PATTERN);
        if (keys.isEmpty()) {
            return Optional.empty();
        }
        String redisLastRatesDateRaw = keys.iterator().next();
        return Optional.of(LocalDate.parse(redisLastRatesDateRaw));
    }

    @Override
    public void unlock() {
        redisTemplate.delete(LOCK_KEY);
    }

    @Override
    public boolean lock() {
        Boolean locked = redisTemplate.opsForValue()
                .setIfAbsent(LOCK_KEY, "locked", LOCK_TTL_SECONDS, TimeUnit.SECONDS);
        return locked != null && locked;
    }
}
