package com.leverx.learningmanagementsystem.course.job.service;

import com.leverx.learningmanagementsystem.email.service.impl.EmailService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class CourseNotificationSender {

    private final EmailService emailService;

    @Async("courseNotificationThreadPoolExecutor")
    public void send(String email, String subject, String body) {
        try {
            log.info("Email sending started");
            emailService.sendEmail(email, subject, body);
        } catch (MessagingException | MailException ex) {
            log.error(ex.getMessage());
        }
    }
}
