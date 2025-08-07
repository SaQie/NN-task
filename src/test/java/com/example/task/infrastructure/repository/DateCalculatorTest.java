package com.example.task.infrastructure.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DateCalculatorTest {

    @Test
    @DisplayName("Should map saturday to friday")
    void test01() {
        LocalDate saturday = LocalDate.of(2025, 8, 9);
        DateCalculator calc = DateCalculator.create();

        LocalDate result = calc.recognizeDateIfWeekend(saturday);

        assertAll(
                () -> assertEquals(DayOfWeek.FRIDAY, result.getDayOfWeek()),
                () -> assertEquals(LocalDate.of(2025, 8, 8), result)
        );
    }

    @Test
    @DisplayName("Should map sunday to friday")
    void test02() {
        LocalDate sunday = LocalDate.of(2025, 8, 10);
        DateCalculator calc = DateCalculator.create();

        LocalDate result = calc.recognizeDateIfWeekend(sunday);

        assertAll(
                () -> assertEquals(DayOfWeek.FRIDAY, result.getDayOfWeek()),
                () -> assertEquals(LocalDate.of(2025, 8, 8), result)
        );
    }

    @Test
    @DisplayName("Should return the same day if day is not weekend")
    void test03() {
        LocalDate wednesday = LocalDate.of(2025, 8, 6);
        DateCalculator calc = DateCalculator.create();

        LocalDate result = calc.recognizeDateIfWeekend(wednesday);

        assertEquals(wednesday, result);
    }

}