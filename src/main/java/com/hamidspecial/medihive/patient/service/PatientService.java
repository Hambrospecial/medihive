package com.hamidspecial.medihive.patient.service;

import com.hamidspecial.medihive.auth.enums.Role;
import com.hamidspecial.medihive.auth.model.AuthUser;
import com.hamidspecial.medihive.auth.service.AuthService;
import com.hamidspecial.medihive.exception.NotFoundException;
import com.hamidspecial.medihive.patient.dto.PatientRegisterRequest;
import com.hamidspecial.medihive.patient.model.Patient;
import com.hamidspecial.medihive.patient.repository.PatientRepository;
import com.hamidspecial.medihive.util.Result;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final AuthService authService;
    private final ModelMapper modelMapper;

    public Result<Patient> getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("404", "Patient not found"));
        return Result.success(patient);
    }

    public Result<Patient> getAllPatients(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Patient> patientPage = patientRepository.findAll(pageable);
        return Result.success(patientPage.getContent(), page, size, patientPage.getTotalElements());
    }

    public Result<Patient> updatePatient(Long id, PatientRegisterRequest request) {
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new NotFoundException("404", "Patient not found"));
        AuthUser authUser = patient.getAuthUser();
        // Create AuthUser
        modelMapper.map(request, authUser);
        authUser.setUpdatedAt(LocalDateTime.now());
        AuthUser user = authService.saveAuthUser(authUser, Role.PATIENT);
        // Create Patient
        modelMapper.map(request, patient);
        patient.setAuthUser(user);
        patientRepository.save(patient);
        return Result.success(patient);
    }

    public Result<Void> deletePatient(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("404", "Patient not found"));
        patientRepository.delete(patient);
        return Result.success(null);
    }



}
