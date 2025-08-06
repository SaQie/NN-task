package com.example.task.application.exception;

import com.example.task.infrastructure.exception.ClientException;

public class RatesNotExistException extends RuntimeException{

    public RatesNotExistException(String message) {
        super(message);
    }

    public RatesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
