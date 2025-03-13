package com.hamidspecial.medihive.patient.model;

import com.hamidspecial.medihive.auth.model.AuthUser;
import com.hamidspecial.medihive.patient.enums.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "tbl_patient")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "auth_user_id", unique = true, nullable = false)
    private AuthUser authUser;
    private LocalDate dateOfBirth;
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


