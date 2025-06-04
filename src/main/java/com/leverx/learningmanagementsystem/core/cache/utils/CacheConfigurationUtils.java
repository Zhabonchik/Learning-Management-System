package com.leverx.learningmanagementsystem.core.cache.utils;

import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;

import static lombok.AccessLevel.PRIVATE;

@Profile("cloud")
@NoArgsConstructor(access = PRIVATE)
public class CacheConfigurationUtils {

    public static final long EXPIRATION_HOURS = 12;
    public static final long MAXIMUM_SIZE = 1000;
}
