package com.hamidspecial.medihive.notification.model;

import com.hamidspecial.medihive.notification.enums.NotificationType;
import lombok.Getter;

@Getter
public class SmsNotificationRequest extends NotificationRequest {
    private final String recipient;
    private final String message;

    public SmsNotificationRequest(String recipient, String message) {
        super(NotificationType.SMS);
        this.recipient = recipient;
        this.message = message;
    }

}
