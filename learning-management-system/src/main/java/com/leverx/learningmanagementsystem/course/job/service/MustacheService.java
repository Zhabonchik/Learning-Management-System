package com.leverx.learningmanagementsystem.course.job.service;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class MustacheService {

    private static final String GREETING = "greeting";
    private static final String COURSE_MESSAGE = "courseMessage";
    private static final String STARTS = "starts";
    private static final String FOOTER = "footer";

    private final MustacheFactory mustacheFactory;
    private final MessageSource messageSource;

    public MustacheService(MessageSource messageSource) {
        this.mustacheFactory = new DefaultMustacheFactory();
        this.messageSource = messageSource;
    }

    public String processTemplate(String template, Map<String, Object> model, Locale locale) {
        Map<String, String> i18n = new HashMap<>();

        i18n.put(GREETING, messageSource.getMessage(GREETING, null, locale));
        i18n.put(COURSE_MESSAGE, messageSource.getMessage(COURSE_MESSAGE, null, locale));
        i18n.put(STARTS, messageSource.getMessage(STARTS, null, locale));
        i18n.put(FOOTER, messageSource.getMessage(FOOTER, null, locale));

        model.put("i18n", i18n);

        Mustache mustache = mustacheFactory.compile(template);
        StringWriter writer = new StringWriter();

        try {
            mustache.execute(writer, model).flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return writer.toString();
    }
}
