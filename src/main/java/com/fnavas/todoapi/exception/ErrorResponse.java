package com.fnavas.todoapi.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {
    private final int statusCode;
    private final String message;
    private final String error;
    private final LocalDateTime timestamp = LocalDateTime.now();

    public ErrorResponse(int statusCode, String message, String error) {
        this.statusCode = statusCode;
        this.message = message;
        this.error = error;
    }
}
