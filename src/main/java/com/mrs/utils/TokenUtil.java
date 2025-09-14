package com.mrs.utils;

import java.util.UUID;

public class TokenUtil {

    public static String generateSessionId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
