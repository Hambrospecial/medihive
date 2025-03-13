package com.hamidspecial.medihive.workflow;

import com.hamidspecial.medihive.auth.model.AuthUser;
import com.hamidspecial.medihive.patient.model.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_patient_consent")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private AuthUser requester;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConsentStatus status;

    private LocalDateTime requestedAt;
    private LocalDateTime approvedAt;
}

