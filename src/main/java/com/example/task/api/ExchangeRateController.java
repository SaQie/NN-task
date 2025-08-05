package com.example.task.api;

import com.example.task.api.query.ExchangeRateQuery;
import com.example.task.api.response.ExchangeRateResponse;
import com.example.task.application.ExchangeRateService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
