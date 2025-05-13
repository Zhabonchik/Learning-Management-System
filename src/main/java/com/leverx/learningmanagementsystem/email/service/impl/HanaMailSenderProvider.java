package com.leverx.learningmanagementsystem.email.service.impl;

import com.leverx.learningmanagementsystem.config.destination.DestinationService;
import com.leverx.learningmanagementsystem.config.featureflags.FeatureFlagsService;
import com.leverx.learningmanagementsystem.email.MailConfig;
import com.leverx.learningmanagementsystem.email.UserProvidedMailConfig;
import com.leverx.learningmanagementsystem.email.service.MailSenderProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
@AllArgsConstructor
@Slf4j
@Profile("hana")
public class HanaMailSenderProvider implements MailSenderProvider {

    private final DestinationService destinationService;
    private final FeatureFlagsService featureFlagsService;
    private final UserProvidedMailConfig userProvidedMailConfig;

    public JavaMailSender getMailSender() {
        MailConfig mailConfig;
        if (featureFlagsService.getFlag("smtp")) {
            mailConfig = destinationService.getEmailConfig("SmtpDestination");
        } else {
            mailConfig = userProvidedMailConfig;
        }
        return configureMailSender(mailConfig);
    }

    private JavaMailSender configureMailSender(MailConfig config) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        log.info("MailConfig: {}", config);
        mailSender.setHost(config.getHost());
        mailSender.setPort(Integer.parseInt(config.getPort()));
        mailSender.setProtocol(config.getProtocol());
        mailSender.setPassword(config.getPassword());
        mailSender.setUsername(config.getFrom());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }
}
