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
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.SimpleMailMessage;
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

    public final ResourceLoader resourceLoader;

    private static final String senderName = "Paralex App";

    @Value("${mail.from.address}")
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

    public String prepareTemplate(final InputStream file, String fileName, final Map<String, Object> scopes) throws IOException {
        final var m = mustacheFactory.compile(new BufferedReader(new InputStreamReader(file)), fileName);
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

    public void sendBailBondNotification(String adminEmail, String userEmail, String fullName) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, false);

        // Set the From address and name
        messageHelper.setFrom(fromAddress, "Paralex App - Bail Bond Request Notification");

        // Set the recipient and subject
        messageHelper.setTo(adminEmail);
        messageHelper.setSubject("New Bailbond Application from Mobile App");

        // HTML email content
        String body = "<html><body>" +
                "<h3>A new Bail Bond request has been submitted by: " + fullName + "</h3>" +
                "<p><strong>User Email:</strong> " + userEmail + "</p>" +
                "<p>Click the button below to manage this request in the Admin Dashboard:</p>" +
                "<p><a href=\"https://staging.admin.paralexapp.com\" style=\"background-color: #007bff; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;\">Login to Admin Dashboard</a></p>" +
                "<p>Regards,<br>Paralex Team</p>" +
                "</body></html>";

        // Set the email body content as HTML
        messageHelper.setText(body, true);
        // Send the email
        javaMailSender.send(mimeMessage);
    }

    public Resource loadMustacheTemplate(String path) throws IOException {
        log.info("[path] path: {}", path);
        return resourceLoader.getResource(path);
    }

    public void sendLawyerWelcomeEmail(String email, String firstName, String lastName, String password) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromAddress, senderName);
            helper.setTo(email);
            helper.setSubject("Welcome to Paralex Legal Platform");

            String htmlBody = """
                <html>
                <body style="font-family: Arial, sans-serif; color: #333;">
                    <h2>Hello %s %s,</h2>
                    <p>Your lawyer profile has been successfully created on the <strong>Paralex Legal Platform</strong>.</p>
                    
                    <h4>Here are your login credentials:</h4>
                    <ul>
                        <li><strong>Email:</strong> %s</li>
                        <li><strong>Password:</strong> %s</li>
                    </ul>
                    
                    <p>👉 <strong>Please change your password</strong> after your first login for security reasons.</p>
                    
                    <p>Regards,<br/>
                    The Paralex Team</p>
                </body>
                </html>
                """.formatted(firstName, lastName, email, password);

            helper.setText(htmlBody, true); // Send as HTML
            javaMailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Failed to send lawyer welcome email to {}", email, e);
            throw new RuntimeException("Failed to send welcome email", e);
        }
    }


}
