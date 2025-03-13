package com.hamidspecial.medihive.pharmacy.model;

import com.hamidspecial.medihive.auth.model.AuthUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_pharmacist")
public class Pharmacist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "auth_user_id", unique = true, nullable = false)
    private AuthUser authUser;
    private String licenseNumber;
    private String pharmacyName;
    private String pharmacyAddress;
}