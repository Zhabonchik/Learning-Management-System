package com.leverx.learningmanagementsystem.email.service.impl;

import com.leverx.learningmanagementsystem.email.service.MailSenderProvider;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class EmailService {

    private final MailSenderProvider mailSenderProvider;

    public void sendEmail(List<String> recipients, String subject, String body) throws MessagingException {
        log.info("Getting mail sender provider for multiple recipients");
        JavaMailSender mailSender = mailSenderProvider.getMailSender();

        log.info("Preparing message for multiple recipients");
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(recipients.toArray(new String[0]));
        helper.setSubject(subject);
        helper.setText(body, false);

        mailSender.send(message);
    }

    public void sendEmail(String recipient, String subject, String body) throws MessagingException {
        log.info("Getting mail sender provider for single recipient");
        JavaMailSender mailSender = mailSenderProvider.getMailSender();

        log.info("Preparing message for single recipient");
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(recipient);
        helper.setSubject(subject);
        helper.setText(body, false);

        mailSender.send(message);
    }
}
