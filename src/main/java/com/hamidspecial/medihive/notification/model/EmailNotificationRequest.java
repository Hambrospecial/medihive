package com.hamidspecial.medihive.notification.model;

import com.hamidspecial.medihive.notification.enums.NotificationType;
import lombok.Getter;

@Getter
public class EmailNotificationRequest extends NotificationRequest {
    private final String sender;
    private final String subject;
    private final String contentType;
    private final String body;
    private final String to;
    private final String[] cc;
    private final String[] bcc;
    private final String[] attachmentNames;
    private final byte[][] attachments;

    private EmailNotificationRequest(Builder builder) {
        super(NotificationType.EMAIL);
        this.sender = builder.sender;
        this.subject = builder.subject;
        this.contentType = builder.contentType;
        this.body = builder.body;
        this.to = builder.to;
        this.cc = builder.cc;
        this.bcc = builder.bcc;
        this.attachmentNames = builder.attachmentNames;
        this.attachments = builder.attachments;
    }

    public static class Builder {
        private String sender;
        private String subject;
        private String contentType;
        private String body;
        private String to;
        private String[] cc;
        private String[] bcc;
        private String[] attachmentNames;
        private byte[][] attachments;

        public Builder sender(String sender) {
            this.sender = sender;
            return this;
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder to(String to) {
            this.to = to;
            return this;
        }

        public Builder cc(String[] cc) {
            this.cc = cc;
            return this;
        }

        public Builder bcc(String[] bcc) {
            this.bcc = bcc;
            return this;
        }

        public Builder attachmentNames(String[] attachmentNames) {
            this.attachmentNames = attachmentNames;
            return this;
        }

        public Builder attachments(byte[][] attachments) {
            this.attachments = attachments;
            return this;
        }

        public EmailNotificationRequest build() {
            return new EmailNotificationRequest(this);
        }
    }
}

