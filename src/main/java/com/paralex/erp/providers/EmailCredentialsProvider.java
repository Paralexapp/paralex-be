package com.paralex.erp.providers;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class EmailCredentialsProvider {
    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.protocol}")
    private String protocol;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean authentication;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean tlsEnabled;

    @Value("${spring.mail.properties.mail.smtp.debug}")
    private boolean debug;
}
