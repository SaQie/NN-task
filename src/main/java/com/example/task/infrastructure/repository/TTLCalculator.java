package com.example.task.infrastructure.repository;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
final class TTLCalculator {

    private static final int ONE_DAY_IN_SECONDS = 86400;

    static Duration calculateTTL(LocalDate date) {
        long secondsUntilMidnight = Duration.between(LocalDateTime.now(),
                date.plusDays(1).atStartOfDay()).getSeconds();

        if (date.getDayOfWeek() == DayOfWeek.FRIDAY) {
            return Duration.ofSeconds(secondsUntilMidnight + 2 * ONE_DAY_IN_SECONDS);
        }

        return Duration.ofSeconds(secondsUntilMidnight);
    }

    static LocalDate recognizeDateIfWeekend(LocalDate date) {
        if (isSunday(date)) {
            return date.minusDays(2);
        }

        if (isSaturday(date)) {
            return date.minusDays(1);
        }

        return date;
    }

    private static boolean isSunday(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    private static boolean isSaturday(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY;
    }
}
