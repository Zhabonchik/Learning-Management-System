package com.leverx.learningmanagementsystem.core.template.service;

import com.samskivert.mustache.Mustache.Compiler;
import com.samskivert.mustache.Template;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mustache.MustacheResourceTemplateLoader;
import org.springframework.stereotype.Component;

import java.io.Reader;
import java.util.Map;

@AllArgsConstructor
@Component
@Slf4j
public class MustacheTemplateBuilder {

    private final MustacheResourceTemplateLoader mustacheResourceTemplateLoader;
    private final Compiler mustacheCompiler;

    public String build(String templateName, Map<String, String> variables) {
        try {
            Reader templateReader = mustacheResourceTemplateLoader.getTemplate(templateName);
            Template template = mustacheCompiler.compile(templateReader);
            return template.execute(variables);
        } catch (Exception e) {
            throw new RuntimeException("Exception occurred while reading template. ", e);
        }
    }
}
