package com.hamidspecial.medihive.notification;

import com.hamidspecial.medihive.notification.model.NotificationRequest;
import com.hamidspecial.medihive.notification.enums.NotificationType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final List<NotificationProvider> notificationProviders;
    private final Map<NotificationType, NotificationProvider> notificationStrategies = new HashMap<>();

    @PostConstruct
    public void init() {
        for (NotificationProvider notificationProvider : notificationProviders) {
            notificationStrategies.put(notificationProvider.getType(), notificationProvider);
        }
    }

    public void send(NotificationRequest request) {
        NotificationProvider notificationProvider = notificationStrategies.get(request.getType());
        if (notificationProvider != null) {
            notificationProvider.send(request);
        } else {
            throw new IllegalArgumentException("Unsupported notification type: " + request.getType());
        }
    }
}
