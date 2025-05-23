package com.leverx.learningmanagementsystem.email.service;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

@Service
public class MustacheService {

    private final MustacheFactory mustacheFactory;

    public MustacheService() {
        this.mustacheFactory = new DefaultMustacheFactory();
    }

    public String processTemplate(String template, Map<String, Object> model) {
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
