package com.paralex.erp.providers;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class EmailCredentialsProvider {
    @Value("${email.credentials.host}")
    private String host;

    @Value("${email.credentials.port}")
    private int port;

    @Value("${email.credentials.username}")
    private String username;

    @Value("${email.credentials.password}")
    private String password;

    @Value("${email.transport.protocol}")
    private String protocol;

    @Value("${email.settings.auth}")
    private boolean authentication;

    @Value("${email.settings.tls-enabled}")
    private boolean tlsEnabled;

    @Value("${email.settings.debug}")
    private boolean debug;
}
