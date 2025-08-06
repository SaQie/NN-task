package com.example.task.infrastructure.exception;

public class ClientException extends RuntimeException {

    public ClientException(String message) {
        super(message);
    }
}
