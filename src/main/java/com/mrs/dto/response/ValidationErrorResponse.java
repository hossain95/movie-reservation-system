package com.mrs.dto.response;

import org.springframework.http.HttpStatus;

import java.util.Map;

public record ValidationErrorResponse(HttpStatus status, String message, Map<String, String> errors) {

    public static ValidationErrorResponse from(HttpStatus httpStatus, String message, Map<String, String> errors) {
        return new ValidationErrorResponse(httpStatus, message, errors);
    }
}
