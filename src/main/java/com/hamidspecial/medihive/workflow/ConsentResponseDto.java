package com.hamidspecial.medihive.workflow;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ConsentResponseDto {
    private Long id;
    private Long patientId;
    private Long requesterId;
    private String status;
    private LocalDateTime requestedAt;
    private LocalDateTime approvedAt;
}
