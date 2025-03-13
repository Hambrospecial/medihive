package com.hamidspecial.medihive.patient.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

//@Document(collection = "tbl_medical_history")
@Setter
@Getter
public class MedicalHistory {
//    @Id
    private String id;
    private Long patientId; // Foreign key reference to Patient (PostgreSQL)
    private List<String> diagnoses;
    private List<String> treatments;
    private LocalDateTime visitDate;
}
