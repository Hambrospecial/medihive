package com.hamidspecial.medihive.hospital.repository;

import com.hamidspecial.medihive.hospital.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
