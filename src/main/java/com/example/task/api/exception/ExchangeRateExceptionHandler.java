package com.example.task.api.exception;

import com.example.task.application.exception.CodeNotFoundException;
import com.example.task.application.exception.RatesNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.BindException;

@ControllerAdvice
public class ExchangeRateExceptionHandler {

    @ExceptionHandler(RatesNotExistException.class)
    public ResponseEntity<String> handleRatesNotExistException(RatesNotExistException e) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .header("Retry-After", "300")
                .body(e.getMessage());
    }

    @ExceptionHandler(CodeNotFoundException.class)
    public ResponseEntity<String> handleCodeNotFoundException(CodeNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<String> onValidationError(BindException ex) {
        String message = ex.getMessage();
        return ResponseEntity.badRequest().body("Request is not valid: " + message);
    }

}
