package com.paralex.erp.services;

import com.github.mustachejava.MustacheFactory;
import com.paralex.erp.dtos.EmailDto;
import com.paralex.erp.dtos.EmailEnvelopeDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Log4j2
public class EmailService {

    private final JavaMailSender javaMailSender;

    private static final String senderName = "Paralex App";

    @Value("${email.service.from-address}")
    private String fromAddress;

//    private final JavaMailSender emailSender;
    private final MustacheFactory mustacheFactory;

    public void sendEmail(@NotNull EmailEnvelopeDto emailEnvelopeDto) throws MessagingException {
        final boolean isHtml = true;
        final MimeMessage message = javaMailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(Objects.requireNonNullElse(emailEnvelopeDto.getFromAddress(), fromAddress));
        helper.setTo(emailEnvelopeDto.getToAddress());
        Optional.ofNullable(emailEnvelopeDto.getCcs())
                .ifPresent(t -> {
                    try {
                        helper.setCc(t.toArray(new String[0]));
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                });
        Optional.ofNullable(emailEnvelopeDto.getBcs())
                .ifPresent(t -> {
                    try {
                        helper.setBcc(t.toArray(new String[0]));
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                });
        helper.setSubject(emailEnvelopeDto.getSubject());
        helper.setText(emailEnvelopeDto.getEmailBody(), isHtml);

        javaMailSender.send(message);
    }

    public String prepareTemplate(final File file, final Map<String, Object> scopes) throws IOException {
        final var m = mustacheFactory.compile(new BufferedReader(new FileReader(file)), file.getName().split("..")[0]);
        final var writer = new StringWriter();

        m.execute(writer, scopes).flush();

        return writer.toString();
    }

    public void sendOtpEmail(EmailDto emailDto) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            var mailMessage = new MimeMessageHelper(message);
            mailMessage.setFrom(fromAddress, senderName);
            mailMessage.setTo(emailDto.getRecipient());
            mailMessage.setSubject(emailDto.getSubject());
            mailMessage.setText(emailDto.getMessageBody(), true);
            javaMailSender.send(message);

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
