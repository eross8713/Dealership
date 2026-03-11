package com.ericross.dealership.advice;

import com.ericross.dealership.dtos.ErrorDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {
    // catches IllegalArgumentException thrown from any controller method, and returns a 400 Bad Request with a JSON body containing the error message
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> handle(IllegalArgumentException ex) {
        return  ResponseEntity.badRequest().body(new ErrorDto(
                Instant.now(),
                400,
                ex.getMessage().toString(),
                null
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
                Instant.now(),
                400,
                bodyStr,
                null
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
                Instant.now(),
                400,
                bodyStr,
                null
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception ex, HttpServletRequest request) {

        ErrorDto error = new ErrorDto(
                Instant.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
