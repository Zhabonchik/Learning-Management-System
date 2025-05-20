package com.leverx.learningmanagementsystem.email.mailsenderprovider.impl;

import com.leverx.learningmanagementsystem.destination.model.MailDestinationConfiguration;
import com.leverx.learningmanagementsystem.destination.service.DestinationService;
import com.leverx.learningmanagementsystem.email.mailsenderprovider.MailSenderProvider;
import com.leverx.learningmanagementsystem.featureflags.service.FeatureFlagsService;
import com.leverx.learningmanagementsystem.email.mailconfig.MailConfig;
import com.leverx.learningmanagementsystem.email.mailconfig.UserProvidedMailConfig;
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
@Profile("cloud")
public class CloudMailSenderProvider implements MailSenderProvider {

    private final String MAIL_SENDER_DESTINATION = "SmtpDestination";
    private final String SMTP_FLAG = "smtp";

    private final DestinationService destinationService;
    private final FeatureFlagsService featureFlagsService;
    private final UserProvidedMailConfig userProvidedMailConfig;

    public JavaMailSender getMailSender() {
        MailConfig mailConfig;

        if (featureFlagsService.getFlag(SMTP_FLAG)) {
            mailConfig = (MailDestinationConfiguration) destinationService
                    .getByName(MAIL_SENDER_DESTINATION)
                    .getDestinationConfiguration();
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
