package com.example.task.infrastructure.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExchangeRateTableDto(
        LocalDate effectiveDate,
        List<ExchangeRateDto> rates
) {


}
