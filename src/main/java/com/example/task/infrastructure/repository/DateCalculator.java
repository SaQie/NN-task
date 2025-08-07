package com.example.task.infrastructure.repository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
final class DateCalculator {

    public static DateCalculator create() {
        return new DateCalculator();
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
}
