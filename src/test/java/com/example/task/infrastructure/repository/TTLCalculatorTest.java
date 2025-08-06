package com.example.task.infrastructure.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TTLCalculatorTest {

    @Test
    @DisplayName("Should calculate TTL for regular day (not weekend)")
    void test01() {
        // given
        LocalDate anyDate = LocalDate.of(2025, 8, 6);
        TTLCalculator.DateProvider provider = () -> anyDate;

        TTLCalculator calc = TTLCalculator.create(provider);

        // when
        Duration ttl = calc.calculateTTL(anyDate);

        // then
        assertEquals(Duration.ofSeconds(86400), ttl);
    }

    @Test
    @DisplayName("Should calculate TTL for friday and add extra 2 days to TTL")
    void test02() {
        // given
        LocalDate friday = LocalDate.of(2025, 8, 8);
        TTLCalculator.DateProvider provider = () -> friday;

        TTLCalculator calc = TTLCalculator.create(provider);

        // when
        Duration ttl = calc.calculateTTL(friday);

        // then:
        Duration expected = Duration.ofSeconds(86400 + 2 * 86400);
        assertEquals(expected, ttl);
    }

    @Test
    @DisplayName("Should map saturday to friday")
    void test03() {
        LocalDate saturday = LocalDate.of(2025, 8, 9);
        TTLCalculator calc = TTLCalculator.create();

        LocalDate result = calc.recognizeDateIfWeekend(saturday);

        assertAll(
                () -> assertEquals(DayOfWeek.FRIDAY, result.getDayOfWeek()),
                () -> assertEquals(LocalDate.of(2025, 8, 8), result)
        );
    }

    @Test
    @DisplayName("Should map sunday to friday")
    void test04() {
        LocalDate sunday = LocalDate.of(2025, 8, 10);
        TTLCalculator calc = TTLCalculator.create();

        LocalDate result = calc.recognizeDateIfWeekend(sunday);

        assertAll(
                () -> assertEquals(DayOfWeek.FRIDAY, result.getDayOfWeek()),
                () -> assertEquals(LocalDate.of(2025, 8, 8), result)
        );
    }

    @Test
    @DisplayName("Should return the same day if day is not weekend")
    void test05() {
        LocalDate wednesday = LocalDate.of(2025, 8, 6);
        TTLCalculator calc = TTLCalculator.create();

        LocalDate result = calc.recognizeDateIfWeekend(wednesday);

        assertEquals(wednesday, result);
    }

}