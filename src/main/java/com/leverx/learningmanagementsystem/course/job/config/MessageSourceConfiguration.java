package com.leverx.learningmanagementsystem.course.job.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import static java.util.Locale.ENGLISH;

@Configuration
public class MessageSourceConfiguration {

    public static final String CLASS_PATH = "classpath:i18n/message";
    public static final String DEFAULT_ENCODING = "UTF-8";

    @Bean
    public MessageSource messageSource() {
        var messageSource = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename(CLASS_PATH);
        messageSource.setDefaultEncoding(DEFAULT_ENCODING);
        messageSource.setFallbackToSystemLocale(false);
        messageSource.setDefaultLocale(ENGLISH);

        return messageSource;
    }
}
