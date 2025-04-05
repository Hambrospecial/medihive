package com.hamidspecial.medihive.notification.service.email;

import com.hamidspecial.medihive.auth.service.ApplicationParameterService;
import com.hamidspecial.medihive.exception.NotFoundException;
import com.hamidspecial.medihive.notification.model.EmailNotificationRequest;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import static com.hamidspecial.medihive.util.ConstantUtils.EMAIL_SENDER;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final ApplicationParameterService applicationParameterService;
    private final JavaMailSender mailSender;

    public void sendHtmlEmail(EmailNotificationRequest emailMessage) {
        try {
            String emailSender = applicationParameterService.getApplicationParameterByKey(EMAIL_SENDER).getData().getValue();
            if (emailSender == null || emailSender.isBlank()) {
                throw new NotFoundException("404", "Email Sender configuration not found");
            }
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(emailSender);
            helper.setTo(emailMessage.getTo());
            helper.setSubject(emailMessage.getSubject());
            helper.setText(emailMessage.getBody(), true);
            mailSender.send(message);
            log.info("Email sent successfully: subject [{}] to recipient {}", emailMessage.getSubject(), emailMessage.getTo());
        } catch (Exception ex) {
            log.error("Error occurred while sending mail to {}", emailMessage.getTo(), ex);
        }
    }
}
