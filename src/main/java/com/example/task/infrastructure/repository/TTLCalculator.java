package com.example.task.infrastructure.repository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
final class TTLCalculator {

    private static final int ONE_DAY_IN_SECONDS = 86400;
    private final DateProvider dateProvider;

    public static TTLCalculator create() {
        return new TTLCalculator(LocalDate::now);
    }

    public static TTLCalculator create(DateProvider dateProvider) {
        return new TTLCalculator(dateProvider);
    }

    Duration calculateTTL(LocalDate date) {
        // start
        LocalDateTime startOfDay = dateProvider.getDate().atStartOfDay();
        // end
        LocalDateTime nextMidnight = date.plusDays(1).atStartOfDay();
        long secondsUntilMidnight = Duration.between(startOfDay, nextMidnight)
                .getSeconds();

        if (date.getDayOfWeek() == DayOfWeek.FRIDAY) {
            return Duration.ofSeconds(secondsUntilMidnight + 2 * ONE_DAY_IN_SECONDS);
        }

        return Duration.ofSeconds(secondsUntilMidnight);
    }

    LocalDate recognizeDateIfWeekend(LocalDate date) {
        if (isSunday(date)) {
            return date.minusDays(2);
        }

        if (isSaturday(date)) {
            return date.minusDays(1);
        }

        return date;
    }

    private boolean isSunday(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    private boolean isSaturday(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY;
    }

    interface DateProvider {

        LocalDate getDate();

    }
}
