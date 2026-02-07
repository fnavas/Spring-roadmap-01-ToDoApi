package com.fnavas.ToDoApi.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> TaskNotFoundHandleException(TaskNotFoundException e) {
        log.warn("[TaskNotFoundHandleException]- {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                404,
                "Task not found",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> MethodArgumentNotValidHandleException(MethodArgumentNotValidException e) {
        log.warn("[MethodArgumentNotValidHandleException]- {}", e.getMessage());
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse(e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                400,
                "Validation Error",
                errorMessage
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> ExceptionHandleException(Exception e) {
        log.warn("[ExceptionHandleException]- {}", e.getMessage());
        ErrorResponse errorResponse =  new ErrorResponse(
                500,
                "Internal Server Error",
                e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
