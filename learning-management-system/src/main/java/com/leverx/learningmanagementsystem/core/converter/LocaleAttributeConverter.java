package com.leverx.learningmanagementsystem.core.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Locale;

import static java.util.Objects.nonNull;

@Converter
public class LocaleAttributeConverter implements AttributeConverter<Locale, String> {

    public static final String DEFAULT_LANGUAGE = "en";

    @Override
    public String convertToDatabaseColumn(Locale locale) {
        return nonNull(locale) ? locale.getLanguage() : DEFAULT_LANGUAGE;
    }

    @Override
    public Locale convertToEntityAttribute(String language) {
        return nonNull(language) ? Locale.forLanguageTag(language) : Locale.forLanguageTag(DEFAULT_LANGUAGE);
    }
}
