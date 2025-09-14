package com.mrs.service;

public interface RedisService {
    void saveOtp(String email, String opt);

    void validateOtp(String email, String opt);

    boolean checkOtp(String email);

    void saveSessionId(String email, String sessionId);

    void validateSessionId(String email, String sessionId);
}
