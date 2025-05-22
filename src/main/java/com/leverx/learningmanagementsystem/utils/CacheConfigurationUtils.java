package com.leverx.learningmanagementsystem.utils;

import org.springframework.context.annotation.Profile;

@Profile("cloud")
public class CacheConfigurationUtils {

    public static final long EXPIRATION_HOURS = 12;
    public static final long MAXIMUM_SIZE = 1000;

    private CacheConfigurationUtils() {}
}
