package com.mrs.dto.response;

import org.springframework.http.HttpStatus;

public record SuccessResponse(HttpStatus status, String message) {
    public static SuccessResponse from(HttpStatus httpStatus, String message) {
        return new SuccessResponse(httpStatus, message);
    }
}
