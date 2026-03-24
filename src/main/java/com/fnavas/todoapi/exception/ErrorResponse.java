package com.fnavas.todoapi.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {
    private int statusCode;
    private String message;
    private String error;
    private LocalDateTime timestamp = LocalDateTime.now();

    public ErrorResponse(int statusCode, String message, String error) {
        this.statusCode = statusCode;
        this.message = message;
        this.error = error;
    }
}
