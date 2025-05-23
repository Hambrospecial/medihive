package com.hamidspecial.medihive.workflow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsentRepository extends JpaRepository<Consent, Long> {
    List<Consent> findByPatientIdAndStatus(Long patientId, ConsentStatus status);
}
