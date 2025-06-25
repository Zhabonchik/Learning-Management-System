package com.leverx.learningmanagementsystem.email.service.impl;

import com.leverx.featureflagsstarter.btp.featureflagservice.service.FeatureFlagsService;
import com.leverx.learningmanagementsystem.btp.destinationservice.model.MailDestinationConfiguration;
import com.leverx.learningmanagementsystem.btp.destinationservice.service.DestinationService;
import com.leverx.learningmanagementsystem.email.service.MailSenderProvider;
import com.leverx.learningmanagementsystem.email.model.MailConfig;
import com.leverx.learningmanagementsystem.email.model.MailConfigUserProvided;
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

    public static final String MAIL_SENDER_DESTINATION = "SmtpDestination";
    public static final String SMTP_FLAG = "smtp";

    private final DestinationService destinationService;
    private final FeatureFlagsService featureFlagsService;
    private final MailConfigUserProvided mailConfigUserProvided;

    public JavaMailSender getMailSender() {
        MailConfig mailConfig;

        if (featureFlagsService.getFlag(SMTP_FLAG)) {
            mailConfig = (MailDestinationConfiguration) destinationService
                    .getByName(MAIL_SENDER_DESTINATION);
        } else {
            mailConfig = mailConfigUserProvided;
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
