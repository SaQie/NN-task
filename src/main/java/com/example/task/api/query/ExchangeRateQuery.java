package com.example.task.api.query;

import jakarta.validation.constraints.NotBlank;

public record ExchangeRateQuery(
        @NotBlank(message = "from parameter cannot be null or empty or blank") String from,
        @NotBlank(message = "to parameter cannot be null or empty or blank") String to) {
}
