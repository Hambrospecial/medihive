package com.hamidspecial.medihive.emailservice.service;

import com.hamidspecial.medihive.auth.service.ApplicationParameterService;
import com.hamidspecial.medihive.emailservice.model.EmailDetails;
import com.hamidspecial.medihive.exception.NotFoundException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import static com.hamidspecial.medihive.util.ConstantUtils.EMAIL_SENDER;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final ApplicationParameterService applicationParameterService;
    private final JavaMailSender javaMailSender;

    public void sendEmail(EmailDetails emailDetails) {
        try {
            String emailSender = applicationParameterService.getApplicationParameterByKey(EMAIL_SENDER).getData().getValue();
            if (emailSender == null || emailSender.isBlank()) {
                throw new NotFoundException("404", "Email Sender configuration not found");
            }
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(emailSender);
            mailMessage.setTo(emailDetails.getToAddress());
            mailMessage.setSubject(emailDetails.getSubject());
            mailMessage.setText(emailDetails.getBody());
            mailMessage.setReplyTo("hamidspecial@gmail.com");
            // Sending the mail
            javaMailSender.send(mailMessage);
            log.info("Mail sent successfully to {}", emailDetails.getToAddress());
        } catch (Exception ex) {
            log.error("Error occurred while sending mail to {}", emailDetails.getToAddress(), ex);
        }
    }


    public void sendHtmlEmail(EmailDetails emailDetails) {
        try {
            String emailSender = applicationParameterService.getApplicationParameterByKey(EMAIL_SENDER).getData().getValue();
            if (emailSender == null || emailSender.isBlank()) {
                throw new NotFoundException("404", "Email Sender configuration not found");
            }
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(emailSender);
            helper.setTo(emailDetails.getToAddress());
            helper.setSubject(emailDetails.getSubject());
            helper.setText(emailDetails.getBody(), true);
            javaMailSender.send(message);
            log.info("Mail sent successfully to {}", emailDetails.getToAddress());
        } catch (Exception ex) {
            log.error("Error occurred while sending mail to {}", emailDetails.getToAddress(), ex);
        }
    }
}
