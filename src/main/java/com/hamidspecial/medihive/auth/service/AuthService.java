package com.hamidspecial.medihive.auth.service;

import com.hamidspecial.medihive.auth.dto.AuthResponse;
import com.hamidspecial.medihive.auth.dto.LoginRequest;
import com.hamidspecial.medihive.auth.dto.RegisterRequest;
import com.hamidspecial.medihive.auth.enums.Role;
import com.hamidspecial.medihive.auth.jwt.JWTService;
import com.hamidspecial.medihive.auth.model.AuthUser;
import com.hamidspecial.medihive.auth.repository.UserRepository;
import com.hamidspecial.medihive.auth.security.UserPrincipal;
import com.hamidspecial.medihive.emailservice.service.EmailService;
import com.hamidspecial.medihive.exception.DuplicateException;
import com.hamidspecial.medihive.hospital.dto.DoctorRegisterRequest;
import com.hamidspecial.medihive.hospital.model.Doctor;
import com.hamidspecial.medihive.hospital.repository.DoctorRepository;
import com.hamidspecial.medihive.patient.dto.PatientRegisterRequest;
import com.hamidspecial.medihive.patient.model.Patient;
import com.hamidspecial.medihive.patient.repository.PatientRepository;
import com.hamidspecial.medihive.pharmacy.dto.PharmacistRegisterRequest;
import com.hamidspecial.medihive.pharmacy.model.Pharmacist;
import com.hamidspecial.medihive.pharmacy.repository.PharmacistRepository;
import com.hamidspecial.medihive.util.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PharmacistRepository pharmacistRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final RedisTemplate<String, String> redisTemplate;
    private final EmailService emailService;
    private final MailContent mailContent;


    @Transactional
    public Result<AuthResponse> login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            AuthUser user = userPrincipal.getAuthUser();
            // Invalidate previous token
            redisTemplate.delete(userPrincipal.getUsername());
            // Generate new token
            String token = jwtService.generateToken(user.getUsername());
            AuthResponse authResponse = new AuthResponse();
            authResponse.setFullName(user.getFirstName() + " " + user.getLastName());
            authResponse.setEmail(user.getEmail());
            authResponse.setRole(user.getRole().name());
            authResponse.setAccessToken(token);
            authResponse.setRefreshToken(UUID.randomUUID().toString());

            return Result.success(authResponse);
        } catch (AuthenticationException e) {
            return Result.error("INVALID_CREDENTIALS", "Invalid username or password");
        }
    }



    @Transactional
    public Result<PatientRegisterRequest> registerPatient(PatientRegisterRequest request) {
        // Create AuthUser
        AuthUser user = saveAuthUser(request, Role.PATIENT);
        // Create Patient
        Patient patient = new Patient();
        patient.setAuthUser(user);
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender());
        patient.setBloodGroup(request.getBloodGroup());
        patient.setGenotype(request.getGenotype());
        patient.setAllergies(request.getAllergies());
        patient.setPreExistingConditions(request.getPreExistingConditions());
        patient.setCurrentMedications(request.getCurrentMedications());
        patient.setEmergencyContactName(request.getEmergencyContactName());
        patient.setEmergencyContactPhone(request.getEmergencyContactPhone());
        patient.setConsentToShareData(request.isConsentToShareData());
        patientRepository.save(patient);
        emailService.sendHtmlEmail(mailContent.generateActivationMail(user));
        return Result.success(patient.getId());
    }

    @Transactional
    public Result<DoctorRegisterRequest> registerDoctor(DoctorRegisterRequest request) {
        // Create and save AuthUser
        AuthUser user = saveAuthUser(request, Role.DOCTOR);
        // Create and save Doctor
        Doctor doctor = new Doctor();
        doctor.setAuthUser(user);
        doctor.setLicenseNumber(request.getLicenseNumber());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setHospitalName(request.getHospitalName());
        doctor.setHospitalAddress(request.getHospitalAddress());
        doctorRepository.save(doctor);
        emailService.sendHtmlEmail(mailContent.generateActivationMail(user));
        return Result.success(doctor.getId());
    }

    @Transactional
    public Result<PharmacistRegisterRequest> registerPharmacist(PharmacistRegisterRequest request) {
        // Create and save AuthUser
        AuthUser user = saveAuthUser(request, Role.PHARMACIST);
        // Create and save Pharmacist
        Pharmacist pharmacist = new Pharmacist();
        pharmacist.setAuthUser(user);
        pharmacist.setLicenseNumber(request.getLicenseNumber());
        pharmacist.setPharmacyName(request.getPharmacyName());
        pharmacist.setPharmacyAddress(request.getPharmacyAddress());
        pharmacistRepository.save(pharmacist);
        emailService.sendHtmlEmail(mailContent.generateActivationMail(user));
        return Result.success(pharmacist.getId());
    }

    public AuthUser saveAuthUser(RegisterRequest request, Role role) {
        AuthUser user = new AuthUser();
        if (userRepository.existsByUsername(request.getUsername())){
            throw new DuplicateException(HttpStatus.CONFLICT.toString(), "User already exist");
        }
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setOtherName(request.getOtherName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        userRepository.save(user);
        return user;
    }

    public AuthUser saveAuthUser(AuthUser user, Role role) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(role);
        userRepository.save(user);
        return user;
    }
}
