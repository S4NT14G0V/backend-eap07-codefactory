package com.codefactory.appstripe.common.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<String> details = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(this::formatFieldError)
                .toList();

        return ResponseEntity.badRequest().body(buildError("VALIDATION_ERROR", "Payload inválido", details));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildError("BUSINESS_RULE_VIOLATION", ex.getMessage(), List.of(ex.getMessage())));
    }
    @ExceptionHandler(java.util.NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(java.util.NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildError("RESOURCE_NOT_FOUND", ex.getMessage(), List.of(ex.getMessage())));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex) {
        HttpStatus status = ex.getMessage() != null && ex.getMessage().contains("no encontrada")
                ? HttpStatus.NOT_FOUND
                : HttpStatus.INTERNAL_SERVER_ERROR;

        String code = status == HttpStatus.NOT_FOUND ? "RESOURCE_NOT_FOUND" : "INTERNAL_ERROR";

        return ResponseEntity.status(status)
                .body(buildError(code, ex.getMessage(), List.of(ex.getMessage())));
    }

    private ErrorResponse buildError(String code, String message, List<String> details) {
        return ErrorResponse.builder()
                .errorCode(code)
                .message(message)
                .details(details)
                .traceId(UUID.randomUUID().toString())
                .timestamp(Instant.now())
                .build();
    }

    private String formatFieldError(FieldError error) {
        return error.getField() + ": " + error.getDefaultMessage();
    }
}
