package net.toadless.radio.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

public class TimeUtils
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeUtils.class);

    public static final Duration ACCESS_TOKEN_EXPIRY = Duration.ofHours(1);
    public static final Duration REFRESH_TOKEN_EXPIRY = Duration.ofDays(30);

    private TimeUtils()
    {
        //Overrides the default, public, constructor
    }

    public static LocalDateTime nowPlusDuration(Duration duration)
    {
        return LocalDateTime.now().plusNanos(duration.toNanos());
    }

    public static Date dateFromLocalDateTime(LocalDateTime time)
    {
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }
}