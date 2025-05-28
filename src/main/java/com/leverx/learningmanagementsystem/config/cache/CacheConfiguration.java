package com.leverx.learningmanagementsystem.config.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.concurrent.TimeUnit;

import static com.leverx.learningmanagementsystem.utils.CacheConfigurationUtils.EXPIRATION_HOURS;
import static com.leverx.learningmanagementsystem.utils.CacheConfigurationUtils.MAXIMUM_SIZE;
import static com.leverx.learningmanagementsystem.utils.HttpConstantUtils.AUTHTOKENS;


@Configuration
@EnableCaching
@Profile("cloud")
public class CacheConfiguration {

    @Bean
    public CacheManager cacheManager() {
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
                .expireAfterWrite(EXPIRATION_HOURS, TimeUnit.HOURS)
                .maximumSize(MAXIMUM_SIZE);

        CaffeineCacheManager cacheManager = new CaffeineCacheManager(AUTHTOKENS);
        cacheManager.setCaffeine(caffeine);

        return cacheManager;
    }
}
