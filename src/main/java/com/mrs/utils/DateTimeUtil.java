package com.mrs.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    public static final DateTimeFormatter DEFAULT_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.of("Asia/Dhaka"));

    public static String format(Instant instant) {
        return DEFAULT_FORMATTER.format(instant);
    }

    public static Instant parse(String dateTimeStr) {
        return Instant.from(DEFAULT_FORMATTER.parse(dateTimeStr));
    }
}
