package com.hamidspecial.medihive.pharmacy.dto;

import com.hamidspecial.medihive.auth.dto.RegisterRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PharmacistRegisterRequest extends RegisterRequest {

    @NotBlank(message = "License number is required")
    private String licenseNumber;

    @NotBlank(message = "Pharmacy name is required")
    private String pharmacyName;

    @NotBlank(message = "Pharmacy address is required")
    private String pharmacyAddress;
}

