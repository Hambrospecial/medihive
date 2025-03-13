package com.hamidspecial.medihive.workflow;

import com.hamidspecial.medihive.util.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/consent")
@RequiredArgsConstructor
public class ConsentController {

    private final ConsentService consentService;

    @PostMapping("/request")
    public ResponseEntity<Result<ConsentResponseDto>> requestConsent(@RequestBody ConsentRequestDto requestDto) {
        return ResponseEntity.ok(Result.success(consentService.requestConsent(requestDto)));
    }

    @PutMapping("/{consentId}/status/{status}")
    public ResponseEntity<Result<ConsentResponseDto>> updateConsentStatus(
            @PathVariable Long consentId,
            @PathVariable ConsentStatus status) {
        return ResponseEntity.ok(Result.success(consentService.updateConsentStatus(consentId, status)));
    }

    @GetMapping("/pending/{patientId}")
    public ResponseEntity<Result<List<ConsentResponseDto>>> getPendingRequests(@PathVariable Long patientId) {
        return ResponseEntity.ok(Result.success(consentService.getPendingRequests(patientId)));
    }
}
