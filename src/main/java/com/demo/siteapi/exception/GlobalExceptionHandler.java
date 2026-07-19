package com.demo.siteapi.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Translates common exceptions into structured, consistent HTTP error
 * responses.
 * <p>
 * All error bodies contain a {@code type} field that clients can use for
 * programmatic handling, plus any additional diagnostic details.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles Jakarta Bean Validation failures.
     * <p>
     * Returns a {@code 400 Bad Request} with a map of field names to their
     * first validation error message.  If the underlying message is
     * {@code null}, a generic fallback is substituted to avoid null values
     * in the map.
     *
     * @param ex the exception containing binding errors
     * @return a {@code 400} response with field-level error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fe -> fe.getDefaultMessage() != null
                                ? fe.getDefaultMessage()
                                : "Invalid value",
                        (first, second) -> first
                ));

        return ResponseEntity.badRequest().body(
                Map.of("type", "validation-error", "errors", fieldErrors));
    }

    /**
     * Handles cases where a requested resource cannot be found.
     *
     * @param ex the exception containing the detail message
     * @return a {@code 404 Not Found} response
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            NoSuchElementException ex) {
        return ResponseEntity.status(404).body(
                Map.of("type", "not-found", "detail", ex.getMessage()));
    }
}