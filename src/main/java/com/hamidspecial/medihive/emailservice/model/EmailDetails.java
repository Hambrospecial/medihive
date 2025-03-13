package com.hamidspecial.medihive.emailservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailDetails {
    private String sender;
    private String subject;
    private String contentType;
    private String body;
    private String toAddress;
    private String[] ccAddress;
    private String[] bccAddress;
    private String[] attachmentNames;
    private byte[][] attachments;
}
