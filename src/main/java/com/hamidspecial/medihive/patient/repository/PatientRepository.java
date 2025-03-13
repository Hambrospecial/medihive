package com.hamidspecial.medihive.patient.repository;

import com.hamidspecial.medihive.patient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
