package com.leverx.learningmanagementsystem.email;

public interface MailConfig {
    String getFrom();
    String getPassword();
    String getProtocol();
    String getHost();
    String getPort();
}
