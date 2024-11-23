package com.paralex.erp.services;

import com.paralex.erp.dtos.EmailCredentialsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@RequiredArgsConstructor
@Service
@Log4j2
public class EmailClientService {
    private final EmailCredentialsDto emailCredentialsDto;

    @Bean
    public JavaMailSender emailSender() {
        final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setUsername(emailCredentialsDto.getUsername());
        mailSender.setPassword(emailCredentialsDto.getPassword());

        final Properties props = getProperties(mailSender);

        mailSender.setJavaMailProperties(props);

        return mailSender;
    }

    @NotNull
    private Properties getProperties(JavaMailSenderImpl mailSender) {
        final Properties props = mailSender.getJavaMailProperties();

        props.put("mail.smtp.host", emailCredentialsDto.getHost());
        props.put("mail.smtp.port", emailCredentialsDto.getPort());
        props.put("mail.transport.protocol", emailCredentialsDto.getProtocol());
        props.put("mail.smtp.auth", emailCredentialsDto.isAuthentication());
        props.put("mail.smtp.starttls.enable", emailCredentialsDto.isTlsEnabled());
        props.put("mail.debug", emailCredentialsDto.isDebug());

        return props;
    }
}
