package com.hamidspecial.medihive.workflow;

import com.hamidspecial.medihive.auth.model.AuthUser;
import com.hamidspecial.medihive.auth.repository.UserRepository;
import com.hamidspecial.medihive.exception.NotFoundException;
import com.hamidspecial.medihive.patient.model.Patient;
import com.hamidspecial.medihive.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsentService {

    private final ConsentRepository consentRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    // Request consent from the patient
    public ConsentResponseDto requestConsent(ConsentRequestDto requestDto) {
        Patient patient = patientRepository.findById(requestDto.getPatientId())
                .orElseThrow(() -> new NotFoundException("404", "Patient not found"));

        AuthUser requester = userRepository.findById(requestDto.getRequesterId())
                .orElseThrow(() -> new NotFoundException("404", "Requester not found"));

        Consent consent = Consent.builder()
                .patient(patient)
                .requester(requester)
                .status(ConsentStatus.PENDING)
                .requestedAt(LocalDateTime.now())
                .build();

        Consent savedConsent = consentRepository.save(consent);
        return mapToResponse(savedConsent);
    }

    // Approve or Reject Consent
    public ConsentResponseDto updateConsentStatus(Long consentId, ConsentStatus status) {
        Consent consent = consentRepository.findById(consentId)
                .orElseThrow(() -> new NotFoundException("404", "Consent request not found"));

        if (status == ConsentStatus.APPROVED) {
            consent.setApprovedAt(LocalDateTime.now());
        }
        consent.setStatus(status);
        
        Consent updatedConsent = consentRepository.save(consent);
        return mapToResponse(updatedConsent);
    }

    // Get pending requests for a patient
    public List<ConsentResponseDto> getPendingRequests(Long patientId) {
        List<Consent> pendingRequests = consentRepository.findByPatientIdAndStatus(patientId, ConsentStatus.PENDING);
        return pendingRequests.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    // Helper: Map entity to DTO
    private ConsentResponseDto mapToResponse(Consent consent) {
        return ConsentResponseDto.builder()
                .id(consent.getId())
                .patientId(consent.getPatient().getId())
                .requesterId(consent.getRequester().getId())
                .status(consent.getStatus().name())
                .requestedAt(consent.getRequestedAt())
                .approvedAt(consent.getApprovedAt())
                .build();
    }
}
