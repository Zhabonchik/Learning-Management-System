package com.leverx.learningmanagementsystem.destination.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leverx.learningmanagementsystem.email.mailconfig.MailConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailDestinationConfiguration extends DestinationConfiguration implements MailConfig {

    @JsonProperty("mail.transport.protocol")
    private String protocol;

    @JsonProperty("mail.smtp.port")
    private String port;

    @JsonProperty("mail.smtp.host")
    private String host;

    @JsonProperty("mail.smtp.from")
    private String from;

    @JsonProperty("mail.smtp.password")
    private String smtpPassword;

    @JsonProperty("mail.password")
    private String password;

    @JsonProperty("mail.user")
    private String user;

    @JsonProperty("Authentication")
    private String authentication;

    @JsonProperty("ProxyType")
    private String proxyType;
}
