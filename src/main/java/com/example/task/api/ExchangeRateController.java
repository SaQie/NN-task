package com.example.task.api;

import com.example.task.api.query.ExchangeRateQuery;
import com.example.task.api.response.ExchangeRateResponse;
import com.example.task.application.ExchangeRateService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rates")
@AllArgsConstructor
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;


    @GetMapping
    public ResponseEntity<ExchangeRateResponse> exchangeRate(@ModelAttribute ExchangeRateQuery query) {
        ExchangeRateResponse response = exchangeRateService.process(query);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/synchronize")
    public ResponseEntity<String> synchronizeManually() {
        // Endpoint only for user with admin role in the future
        exchangeRateService.synchronizeManually();
        return ResponseEntity.ok("Synchronized");
    }

}
