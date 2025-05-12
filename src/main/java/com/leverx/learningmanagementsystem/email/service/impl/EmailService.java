package com.leverx.learningmanagementsystem.email.service.impl;

import com.leverx.learningmanagementsystem.email.MailConfig;
import com.leverx.learningmanagementsystem.email.service.MailSenderProvider;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class EmailService {

    private final MailSenderProvider mailSenderProvider;

    public void sendEmail(List<String> recipients, String subject, String body) throws MessagingException {
        log.info("Getting mail sender provider");
        JavaMailSender mailSender = mailSenderProvider.getMailSender();

        log.info("Preparing message");
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(recipients.toArray(new String[0]));
        helper.setSubject(subject);
        helper.setText(body, false);

        log.info("Sending email with port {}, host {}, username {}, protocol {}",
                ((JavaMailSenderImpl) mailSender).getPort(),
                ((JavaMailSenderImpl) mailSender).getHost(),
                ((JavaMailSenderImpl) mailSender).getUsername(),
                ((JavaMailSenderImpl) mailSender).getProtocol());

        mailSender.send(message);
    }
}
