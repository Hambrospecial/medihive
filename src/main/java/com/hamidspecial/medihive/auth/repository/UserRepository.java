package com.hamidspecial.medihive.auth.repository;

import com.hamidspecial.medihive.auth.enums.Role;
import com.hamidspecial.medihive.auth.model.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AuthUser, Long> {
    Optional<AuthUser> findByUsername(String username);
    boolean existsByRole(Role role);
    Optional<AuthUser> findByEmail(String email);
    Optional<AuthUser> findByPassword(String password);
    boolean existsByUsername(String username);
}
