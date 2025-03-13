package com.hamidspecial.medihive.auth.service;

import com.hamidspecial.medihive.auth.enums.Role;
import com.hamidspecial.medihive.auth.model.ApplicationParameter;
import com.hamidspecial.medihive.auth.model.AuthUser;
import com.hamidspecial.medihive.auth.repository.ApplicationParameterRepository;
import com.hamidspecial.medihive.auth.repository.UserRepository;
import com.hamidspecial.medihive.util.ObjectSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.hamidspecial.medihive.util.ConstantUtils.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class SuperAdminInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final ApplicationParameterRepository applicationParameterRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (!userRepository.existsByRole(Role.SUPER_ADMIN)) {
            AuthUser superAdmin = new AuthUser();
            superAdmin.setFirstName("admin");
            superAdmin.setLastName("admin");
            superAdmin.setUsername("superadmin");
            superAdmin.setEmail("superadmin@medihive.com");
            superAdmin.setPassword(passwordEncoder.encode("Admin@123"));
            superAdmin.setPhoneNumber("+234809090890");
            superAdmin.setRole(Role.SUPER_ADMIN);
            userRepository.save(superAdmin);
            log.info("✅ Super Admin account created!");
        }

        PasswordCheckList passwordCheckList = new PasswordCheckList();
        Map<String, String> applicationParams = Map.of(
                HOST_IP, "http://localhost:9091/api/v1/",
                EMAIL_SENDER, "no_reply@medihive.com",
                PASSWORD_POLICY, ObjectSerializer.serializeToJson(passwordCheckList)
        );
        applicationParams.forEach((key, value) -> {
            if (!applicationParameterRepository.existsByParamKey(key)) {
                ApplicationParameter applicationParameter = new ApplicationParameter();
                applicationParameter.setName(key);
                applicationParameter.setParamKey(key);
                applicationParameter.setValue(value);
                applicationParameter.setDescription(value);
                applicationParameterRepository.save(applicationParameter);
            }
        });

    }
}

