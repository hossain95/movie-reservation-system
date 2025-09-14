package com.mrs.dto.response;

public record SuccessOtpVerificationResponse(String sessionId) {
    public static SuccessOtpVerificationResponse from(String sessionId) {
        return new SuccessOtpVerificationResponse(sessionId);
    }
}
