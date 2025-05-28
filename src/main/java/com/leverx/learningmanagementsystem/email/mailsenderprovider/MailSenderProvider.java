package com.leverx.learningmanagementsystem.email.mailsenderprovider;

import org.springframework.mail.javamail.JavaMailSender;

public interface MailSenderProvider {
    JavaMailSender getMailSender();
}
