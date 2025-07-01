package com.leverx.learningmanagementsystem.email.service.impl;

import com.leverx.learningmanagementsystem.core.template.service.LocalizedMessageProvider;
import com.leverx.learningmanagementsystem.core.template.service.MustacheTemplateBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class EmailTemplateBuilder {

    private static final String COURSE_STARTING_TEMPLATE_NAME = "email/course_reminder";
    private static final String GREETING = "greeting";
    private static final String COURSE_MESSAGE = "courseMessage";
    private static final String STARTS = "starts";
    private static final String FOOTER = "footer";

    private final MustacheTemplateBuilder mustacheTemplateBuilder;
    private final LocalizedMessageProvider localizedMessageProvider;

    public String buildForCourseStarting(Map<String, String> context) {
        Map<String, String> templateContext = buildCourseStartingTemplateContext(context);
        return mustacheTemplateBuilder.build(COURSE_STARTING_TEMPLATE_NAME, templateContext);
    }

    private Map<String, String> buildCourseStartingTemplateContext(Map<String, String> context) {
        Map<String, String> templateContext = new HashMap<>(context);

        templateContext.put(GREETING, localizedMessageProvider.getLocalizedMessage(GREETING));
        templateContext.put(COURSE_MESSAGE, localizedMessageProvider.getLocalizedMessage(COURSE_MESSAGE));
        templateContext.put(STARTS, localizedMessageProvider.getLocalizedMessage(STARTS));
        templateContext.put(FOOTER, localizedMessageProvider.getLocalizedMessage(FOOTER));

        return templateContext;
    }
}
