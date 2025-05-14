package com.leverx.learningmanagementsystem.utils;

import org.springframework.context.annotation.Profile;

@Profile("cloud")
public class RetryUtils {

    public static final long DELAY = 1000;
    public static final int MAX_ATTEMPTS = 3;

    private RetryUtils() {}
}
