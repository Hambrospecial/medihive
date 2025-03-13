package com.hamidspecial.medihive.patient.dto;

import com.hamidspecial.medihive.auth.dto.RegisterRequest;
import com.hamidspecial.medihive.patient.enums.Gender;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PatientRegisterRequest extends RegisterRequest {
    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;
    @NotNull(message = "Gender is required")
    private Gender gender;
    private String bloodGroup;
    private String genotype;
    private String allergies;
    private String preExistingConditions;
    private String currentMedications;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private boolean consentToShareData;
}

