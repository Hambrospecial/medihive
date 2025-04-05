package com.hamidspecial.medihive.patient.controller;

import com.hamidspecial.medihive.patient.dto.PatientDto;
import com.hamidspecial.medihive.patient.dto.PatientRegisterRequest;
import com.hamidspecial.medihive.patient.model.Patient;
import com.hamidspecial.medihive.patient.service.PatientService;
import com.hamidspecial.medihive.util.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
@Slf4j
public class PatientController {

    private final PatientService patientService;

    @GetMapping("/{id}")
    public ResponseEntity<Result<Patient>> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @GetMapping("/profile")
    public ResponseEntity<Result<Patient>> getPatientProfile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return ResponseEntity.ok(patientService.getPatientByUsername(authentication));
        } catch (Exception e) {
            log.error("Profile endpoint error", e);
            return ResponseEntity.internalServerError()
                    .body(Result.error("500", "Internal server error"));
        }
    }

    @GetMapping
    public ResponseEntity<Result<Patient>> getAllPatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(patientService.getAllPatients(page, size));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result<Patient>> updatePatient(
            @PathVariable Long id,
            @RequestBody @Valid PatientRegisterRequest patientRequest) {
        return ResponseEntity.ok(patientService.updatePatient(id, patientRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Result<Void>> deletePatient(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.deletePatient(id));
    }
}

