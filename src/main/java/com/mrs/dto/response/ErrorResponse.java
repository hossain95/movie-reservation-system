package com.mrs.dto.response;


import org.springframework.http.HttpStatus;

public record ErrorResponse(HttpStatus status, String message) {
    public static ErrorResponse from(HttpStatus httpStatus, String message) {
        return new ErrorResponse(httpStatus, message);
    }
}