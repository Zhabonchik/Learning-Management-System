package com.leverx.learningmanagementsystem.core.template.service;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@AllArgsConstructor
public class LocalizedMessageProvider {

    private final MessageSource messageSource;

    public String getLocalizedMessage(String key, Object ... args) {
        Locale messageLocale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, args, messageLocale);
    }

}
