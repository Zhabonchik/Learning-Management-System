package com.leverx.learningmanagementsystem.email;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ConfigurationProperties("user-provided.mail.smtp")
@Data
@Profile("hana")
public class UserProvidedMailConfig implements MailConfig {
    @JsonProperty("mail.transport.protocol")
    private String protocol;
    @JsonProperty("mail.smtp.port")
    private String port;
    @JsonProperty("mail.smtp.host")
    private String host;
    @JsonProperty("mail.smtp.from")
    private String from;
    @JsonProperty("mail.smtp.password")
    private String password;
}
