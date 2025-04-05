package com.hamidspecial.medihive.notification.service.email;

import com.hamidspecial.medihive.notification.enums.NotificationType;
import com.hamidspecial.medihive.notification.model.NotificationRequest;
import com.hamidspecial.medihive.notification.model.EmailNotificationRequest;
import com.hamidspecial.medihive.notification.NotificationProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailNotificationService implements NotificationProvider {

    private final EmailService emailService;

    @Autowired
    public EmailNotificationService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void send(NotificationRequest request) {
        if (!(request instanceof EmailNotificationRequest emailRequest)) {
            throw new IllegalArgumentException("Invalid request type for Email Notification");
        }
        log.info("Sending Email to {}", emailRequest.getTo());
        emailService.sendHtmlEmail(emailRequest);
    }

    @Override
    public NotificationType getType() {
        return NotificationType.EMAIL;
    }
}
