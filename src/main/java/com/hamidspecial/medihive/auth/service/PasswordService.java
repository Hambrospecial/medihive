package com.hamidspecial.medihive.auth.service;

import com.hamidspecial.medihive.auth.dto.ChangePasswordRequest;
import com.hamidspecial.medihive.auth.dto.ResetPasswordRequest;
import com.hamidspecial.medihive.auth.jwt.JWTService;
import com.hamidspecial.medihive.auth.model.AuthUser;
import com.hamidspecial.medihive.auth.repository.UserRepository;
import com.hamidspecial.medihive.emailservice.model.EmailDetails;
import com.hamidspecial.medihive.emailservice.service.EmailService;
import com.hamidspecial.medihive.exception.BadRequestException;
import com.hamidspecial.medihive.exception.InvalidTokenException;
import com.hamidspecial.medihive.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hamidspecial.medihive.util.ConstantUtils.*;

@Service
@RequiredArgsConstructor
public class PasswordService {

    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;
    private final EmailService emailService;
    private final PasswordPolicyService passwordPolicyService;
    private final ApplicationParameterService applicationParameterService;

    /**
     * Sends a password reset link to the user's email.
     *
     * @param email User's email
     * @return Status code (SUCCESS = 0, USER_NOT_FOUND = 1)
     */
    public void sendResetLink(String email) {
        AuthUser user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("404", "User not found"));
        // Generate token and store in Redis with expiry time
        String resetLinkKey = jwtService.generateTokenLinkKey(user.getUsername());
        // Fetch necessary configurations
        String hostIp = applicationParameterService.getApplicationParameterByKey(HOST_IP).getData().getValue();
        // Validate configurations
        if (hostIp == null || hostIp.isBlank()) {
            throw new NotFoundException("404", "Host IP configuration not found");
        }
        // Construct password reset link
        String resetLink = hostIp + "/password/reset/" + resetLinkKey;
        // Send email
        EmailDetails emailDetails = EmailDetails.builder()
                .subject("Password Reset Link")
                .contentType("text/plain")
                .body("Click the following link to reset your password: " + resetLink)
                .toAddress(email)
                .build();
        emailService.sendEmail(emailDetails);
    }


    /**
     * Resets the password using a token.
     * @param request ResetPasswordRequest containing new password and token.
     */
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        validatePasswords(request.getNewPassword(), request.getConfirmPassword());
        passwordPolicyService.validatePassword(request.getNewPassword());
        // Retrieve token from Redis
        String token = redisTemplate.opsForValue().get(request.getToken());
        if (token == null) {
            throw new InvalidTokenException("Invalid or expired reset token.");
        }
        String username = jwtService.extractUsername(token);
        AuthUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("400", "User not found."));
        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // Invalidate the reset token after usage
        redisTemplate.delete(request.getToken());
    }

    /**
     * Changes the user's password.
     * @param request ChangePasswordRequest containing current and new passwords.
     */
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        validatePasswords(request.getNewPassword(), request.getConfirmPassword());
        passwordPolicyService.validatePassword(request.getNewPassword());
        AuthUser user = userRepository.findByPassword(passwordEncoder.encode(request.getCurrentPassword()))
                .orElseThrow(() -> new BadRequestException("400", "Incorrect current password."));
        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    /**
     * Validates new passwords.
     * @param newPassword New password
     * @param confirmPassword Confirmation password
     */
    private void validatePasswords(String newPassword, String confirmPassword) {
        if (newPassword == null || !newPassword.equals(confirmPassword)) {
            throw new BadRequestException("400", "Passwords do not match.");
        }
    }
}
