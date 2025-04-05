package com.hamidspecial.medihive.auth.controller;

import com.hamidspecial.medihive.auth.dto.AuthResponse;
import com.hamidspecial.medihive.auth.dto.LoginRequest;
import com.hamidspecial.medihive.auth.service.AuthService;
import com.hamidspecial.medihive.hospital.dto.DoctorRegisterRequest;
import com.hamidspecial.medihive.patient.dto.PatientRegisterRequest;
import com.hamidspecial.medihive.pharmacy.dto.PharmacistRegisterRequest;
import com.hamidspecial.medihive.util.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Result<AuthResponse> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register/patient")
    public Result<PatientRegisterRequest> registerPatient(@Valid @RequestBody PatientRegisterRequest request) {
        return authService.registerPatient(request);
    }

    @PostMapping("/register/doctor")
    public Result<DoctorRegisterRequest> registerDoctor(@Valid @RequestBody DoctorRegisterRequest request) {
        return authService.registerDoctor(request);
    }

    @PostMapping("/register/pharmacist")
    public Result<PharmacistRegisterRequest> registerPharmacist(@Valid @RequestBody PharmacistRegisterRequest request) {
        return authService.registerPharmacist(request);
    }

}

