package com.spring.rms.services.serviceImpls;

import com.spring.rms.models.Resource;
import com.spring.rms.models.User;
import com.spring.rms.repositories.IUserRepository;
import com.spring.rms.services.IMailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements IMailService {

    private final JavaMailSender mailSender;
    private final IUserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    @Value("${spring.mail.username}")
    private String from;

    @Value("${spring.mail.username}")
    private String defaultAdminEmail;

    @Override
    public void sendResourceCreatedEmail(String username, Resource resource) {
        sendEmail(username, resource, "resource-created.html", "Resource Created");
    }

    @Override
    public void sendResourceUpdatedEmail(String username, Resource resource) {
        sendEmail(username, resource, "resource-updated.html", "Resource Updated");
    }

    @Override
    public void sendResourceDeletedEmail(String username, Resource resource) {
        sendEmail(username, resource, "resource-deleted.html", "Resource Deleted");
    }

    private void sendEmail(String username, Resource resource, String templatePath, String subject) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

            helper.setTo(user.getEmail());
            helper.setSubject(subject);


            helper.setFrom(from);

            String htmlContent = loadEmailTemplate(templatePath)
                    .replace("{{username}}", username)
                    .replace("{{resourceName}}", resource.getName())
                    .replace("{{category}}", resource.getCategory().toString());

            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException | IOException e) {
            e.printStackTrace(); // prints full stack trace to console
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }

    }

    @Override
    public void sendBookingStatusEmail(String username, Resource resource, String action, String status) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setTo(user.getEmail());
            helper.setSubject("Your booking has been " + action + ": " + resource.getName());

            String htmlContent = loadEmailTemplate("booking-status.html")
                    .replace("{{username}}", username)
                    .replace("{{resourceName}}", resource.getName())
                    .replace("{{action}}", action)
                    .replace("{{status}}", status);

            helper.setText(htmlContent, true);
            helper.setFrom(from);
            mailSender.send(message);

            logger.info("Email sent to {}", user.getEmail());
        } catch (MessagingException e) {
            logger.error("Email sending failed due to messaging error", e);
            throw new RuntimeException("Messaging error while sending email", e);
        } catch (IOException e) {
            logger.error("Email sending failed due to template loading error", e);
            throw new RuntimeException("Failed to load email template", e);
        }
    }

    @Override
    public void sendBookingStatusEmailToDefault(Resource resource, String action, String status) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setTo(defaultAdminEmail);
            helper.setSubject("Booking " + action);
            String htmlContent = loadEmailTemplate("booking-status.html")
                    .replace("{{username}}", "Admin")
                    .replace("{{resourceName}}", resource.getName())
                    .replace("{{action}}", action)
                    .replace("{{status}}", status);
            helper.setText(htmlContent, true);
            helper.setFrom(from);
            mailSender.send(message);
            logger.info("Email sent to default admin: {}", defaultAdminEmail);
        } catch (MessagingException | IOException e) {
            logger.error("Failed to send email to default admin {}: {}", defaultAdminEmail, e.getMessage(), e);
            throw new RuntimeException("Failed to send email", e);
        }

    }
    private String loadEmailTemplate(String templatePath) throws IOException {
        try (InputStream is = new ClassPathResource("templates/" + templatePath).getInputStream()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

}