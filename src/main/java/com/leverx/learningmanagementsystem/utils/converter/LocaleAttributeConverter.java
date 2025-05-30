package com.leverx.learningmanagementsystem.utils.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Locale;

import static java.util.Objects.isNull;

@Converter
public class LocaleAttributeConverter implements AttributeConverter<Locale, String> {

    private final String DEFAULT_LANGUAGE = "en";

    @Override
    public String convertToDatabaseColumn(Locale locale) {
        return !isNull(locale) ? locale.getLanguage() : DEFAULT_LANGUAGE;
    }

    @Override
    public Locale convertToEntityAttribute(String language) {
        return !isNull(language) ? Locale.forLanguageTag(language) : Locale.forLanguageTag(DEFAULT_LANGUAGE);
    }
}
