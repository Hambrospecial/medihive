package com.hamidspecial.medihive.auth.service;

import com.hamidspecial.medihive.auth.jwt.JWTService;
import com.hamidspecial.medihive.auth.model.AuthUser;
import com.hamidspecial.medihive.emailservice.model.EmailDetails;
import com.hamidspecial.medihive.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.hamidspecial.medihive.util.ConstantUtils.HOST_IP;

@Component
@RequiredArgsConstructor
public class MailContent {

    private final ApplicationParameterService applicationParameterService;
    private final JWTService jwtService;

    public EmailDetails generateActivationMail(AuthUser user) {
        String activationLinkKey = jwtService.generateTokenLinkKey(user.getUsername());
        String hostIp = applicationParameterService.getApplicationParameterByKey(HOST_IP).getData().getValue();

        // Validate configurations
        if (hostIp == null || hostIp.isBlank()) {
            throw new NotFoundException("404", "Host IP configuration not found");
        }

        String activateLink = hostIp + "/account/activate/" + activationLinkKey;
        String fullName = Stream.of(user.getFirstName(), user.getOtherName(), user.getLastName())
                .filter(Objects::nonNull).collect(Collectors.joining(" ")).trim();
        String emailBody = "<html>" +
                "<body style='font-family: Arial, sans-serif;'>" +
                "<h2>Welcome to MediHive, " + fullName + "!</h2>" +
                "<p>Thank you for signing up. To start using your account, please confirm your email address by clicking the button below:</p>" +
                "<p><a href='" + activateLink + "' " +
                "style='display: inline-block; padding: 10px 20px; font-size: 16px; color: #ffffff; " +
                "background-color: #007bff; text-decoration: none; border-radius: 5px;'>Activate My Account</a></p>" +
                "<p>Or copy and paste the following link in your browser:</p>" +
                "<p><a href='" + activateLink + "'>" + activateLink + "</a></p>" +
                "<hr>" +
                "<p><strong>Note:</strong> This activation link will expire in 24 hours. If you did not sign up for MediHive, please ignore this email.</p>" +
                "<p>Best Regards,<br><strong>The MediHive Team</strong></p>" +
                "</body></html>";

        return EmailDetails.builder()
                .subject("Activate Your MediHive Account")
                .toAddress(user.getEmail())
                .contentType("text/html")
                .body(emailBody)
                .build();
    }

}
