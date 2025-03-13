package com.hamidspecial.medihive.workflow;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsentRequestDto {
    private Long patientId;
    private Long requesterId; // Doctor or Pharmacist ID
}

