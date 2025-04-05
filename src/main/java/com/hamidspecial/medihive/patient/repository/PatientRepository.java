package com.hamidspecial.medihive.patient.repository;

import com.hamidspecial.medihive.patient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByAuthUserId(Long id);
}
