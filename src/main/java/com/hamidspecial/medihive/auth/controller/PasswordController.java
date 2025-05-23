package com.hamidspecial.medihive.auth.controller;

import com.hamidspecial.medihive.auth.dto.ChangePasswordRequest;
import com.hamidspecial.medihive.auth.dto.ResetPasswordRequest;
import com.hamidspecial.medihive.auth.service.PasswordService;
import com.hamidspecial.medihive.util.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/password")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordService passwordService;

    @PostMapping("/forgot")
    public Result<String> forgotPassword(@RequestHeader(value = "email") String email) {
        passwordService.sendResetLink(email);
        return Result.success("Password reset Link sent Successfully");
    }

    @PostMapping("/reset")
    public Result<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        passwordService.resetPassword(request);
        return Result.success("Password reset successful");
    }

    @PutMapping("/change")
    public Result<String> changePassword(@RequestBody ChangePasswordRequest request) {
        passwordService.changePassword(request);
        return Result.success("Password changed successfully");
    }
}

