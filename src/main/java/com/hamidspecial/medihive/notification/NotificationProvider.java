package com.hamidspecial.medihive.notification;

import com.hamidspecial.medihive.notification.model.NotificationRequest;
import com.hamidspecial.medihive.notification.enums.NotificationType;

public interface NotificationProvider {
    void send(NotificationRequest request);
    NotificationType getType();
}
