package com.hamidspecial.medihive.notification.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
class EmailServiceTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void sendEmail() {
    }

    @Autowired
    private JavaMailSender mailSender;

    @Test
    void testEmailConnection() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("hamidspecial@gmail.com");
        message.setTo("hamid.okanlawon28@gmail.com");
        message.setSubject("Test Email from Spring Boot");
        message.setText("This is a test email from Spring Boot application.");
        mailSender.send(message);
        // If no exception is thrown, the connection is successful
    }


}