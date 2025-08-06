package com.example.task.application.exception;

public class RatesNotExistException extends RuntimeException{

    public RatesNotExistException(String message) {
        super(message);
    }
}
