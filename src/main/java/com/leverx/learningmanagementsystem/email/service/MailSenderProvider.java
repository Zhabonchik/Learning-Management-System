package com.leverx.learningmanagementsystem.email.service;

import org.springframework.mail.javamail.JavaMailSender;

public interface MailSenderProvider {
    JavaMailSender getMailSender();
}
