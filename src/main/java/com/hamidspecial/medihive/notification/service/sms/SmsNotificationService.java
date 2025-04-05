package com.hamidspecial.medihive.notification.service.sms;

import com.hamidspecial.medihive.notification.model.SmsNotificationRequest;
import com.hamidspecial.medihive.notification.model.NotificationRequest;
import com.hamidspecial.medihive.notification.enums.NotificationType;
import com.hamidspecial.medihive.notification.NotificationProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SmsNotificationService implements NotificationProvider {

    private final SmsService smsService;

    @Autowired
    public SmsNotificationService(SmsService smsService) {
        this.smsService = smsService;
    }

    @Override
    public void send(NotificationRequest request) {
        if (!(request instanceof SmsNotificationRequest smsRequest)) {
            throw new IllegalArgumentException("Invalid request type for Sms Notification");
        }

        log.info("Sending SMS to {}", smsRequest.getRecipient());
        smsService.sendSms(smsRequest.getRecipient(), smsRequest.getMessage());
    }

    @Override
    public NotificationType getType() {
        return NotificationType.SMS;
    }
}
