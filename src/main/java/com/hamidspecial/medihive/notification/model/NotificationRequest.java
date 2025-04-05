package com.hamidspecial.medihive.notification.model;

import com.hamidspecial.medihive.notification.enums.NotificationType;
import lombok.Getter;

@Getter
public abstract class NotificationRequest {
    private final NotificationType type;

    protected NotificationRequest(NotificationType type) {
        this.type = type;
    }

}
