package com.hamidspecial.medihive.auth.enums;

import lombok.Getter;

@Getter
public enum Role {
    SUPER_ADMIN("Super Admin", "System administrator with overall full access to the system"),
    PATIENT("Patient", "A registered individual receiving medical care"),
    DOCTOR("Doctor", "A licensed medical professional treating patients"),
    PHARMACIST("Pharmacist", "A licensed professional dispensing medication"),
    ADMIN("Admin", "System administrator with Hospital or pharmacy-specific admin");

    private final String displayName;
    private final String description;

    Role(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public static Role fromString(String roleName) {
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(roleName)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role: " + roleName);
    }
}
