package com.hamidspecial.medihive.auth.repository;


import com.hamidspecial.medihive.auth.model.ApplicationParameter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicationParameterRepository extends JpaRepository<ApplicationParameter, Long> {
    Optional<ApplicationParameter> findByParamKey(String key);
    boolean existsByParamKey(String key);
}
