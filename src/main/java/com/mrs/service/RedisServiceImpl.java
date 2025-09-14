package com.mrs.service;

import com.mrs.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {
    private final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    private static final String OTP_PREFIX = "OTP";
    private static final String SESSION_PREFIX = "SESSION";

    private final RedisTemplate<String, String> redisTemplate;

    public RedisServiceImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveOtp(String email, String opt) {
        String key = otpKey(email);
        logger.info("Store otp with key: {}", key);
        redisTemplate.opsForValue().set(key, opt, 2, TimeUnit.MINUTES);
    }

    @Override
    public void validateOtp(String email, String otp) {
        String key = otpKey(email);
        logger.info("Validate otp with key: {}", key);

        String savedKey = redisTemplate.opsForValue().get(key);
        if (savedKey != null && savedKey.equals(otp)) {
            logger.info("OTP validation success");
            redisTemplate.delete(key);
            return;
        }
        logger.info("OTP validation failed");
        throw new BadRequestException("Invalid OTP");
    }

    @Override
    public boolean checkOtp(String email) {
        String key = otpKey(email);
        return redisTemplate.hasKey(key);
    }

    @Override
    public void saveSessionId(String email, String sessionId) {
        String key = sessionKey(email);
        logger.info("Store sessionId with key: {}", key);
        redisTemplate.opsForValue().set(key, sessionId, 5, TimeUnit.MINUTES);
    }

    @Override
    public void validateSessionId(String email, String sessionId) {
        String key = sessionKey(email);
        logger.info("Validate sessionId with key: {}", key);
        String savedSessionId = redisTemplate.opsForValue().get(key);
        if (savedSessionId == null || !savedSessionId.equals(sessionId)) {
            logger.info("SessionId validation failed");
            throw new BadRequestException("Invalid Session id");
        }
        redisTemplate.delete(key);
    }

    private String sessionKey(String email) {
        return SESSION_PREFIX + "_" + email;
    }

    private String otpKey(String email) {
        return OTP_PREFIX + "_" + email;
    }
}
