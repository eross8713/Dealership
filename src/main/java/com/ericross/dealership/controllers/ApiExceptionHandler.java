package com.ericross.dealership.controllers;

import com.ericross.dealership.dtos.ErrorDto;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {
    // catches IllegalArgumentException thrown from any controller method, and returns a 400 Bad Request with a JSON body containing the error message
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> handle(IllegalArgumentException ex) {
        return  ResponseEntity.badRequest().body(new ErrorDto(
                java.time.OffsetDateTime.now(),
                400,
                "error: " + ex.getMessage().toString(),
                java.util.UUID.randomUUID().toString()
        ));
    }

    // For @Valid @RequestBody DTO validation failures
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            // If multiple errors per field, keep the first (simple + stable)
            fieldErrors.putIfAbsent(fe.getField(), fe.getDefaultMessage());
        }

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("errors", fieldErrors);
        String bodyStr = body.toString();

        return  ResponseEntity.badRequest().body(new ErrorDto(
                java.time.OffsetDateTime.now(),
                400,
                bodyStr,
                java.util.UUID.randomUUID().toString()
        ));

    }

    // For validation on @RequestParam / @PathVariable when using @Validated
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> violations = new LinkedHashMap<>();
        ex.getConstraintViolations().forEach(v -> {
            String path = v.getPropertyPath().toString(); // e.g. "addCar.arg0" or "id"
            violations.put(path, v.getMessage());
        });

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("errors", violations);
        String bodyStr = body.toString();


        return  ResponseEntity.badRequest().body(new ErrorDto(
                java.time.OffsetDateTime.now(),
                400,
                bodyStr,
                java.util.UUID.randomUUID().toString()
        ));
    }
}
