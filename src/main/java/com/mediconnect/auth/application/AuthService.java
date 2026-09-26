package com.mediconnect.auth.application;

import com.mediconnect.auth.api.*;
import com.mediconnect.auth.domain.Role;
import com.mediconnect.auth.domain.UserEntity;
import com.mediconnect.auth.infrastructure.UserRepository;
import com.mediconnect.patient.api.PatientResponse;
import com.mediconnect.patient.domain.PatientEntity;
import com.mediconnect.patient.infrastructure.PatientMapper;
import com.mediconnect.patient.infrastructure.PatientRepository;
import com.mediconnect.shared.exception.DuplicateResourceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    public AuthService(UserRepository userRepository, PatientRepository patientRepository, PatientMapper patientMapper, PasswordEncoder passwordEncoder, JwtTokenService jwtTokenService) {
        this.userRepository=userRepository; this.patientRepository=patientRepository; this.patientMapper=patientMapper; this.passwordEncoder=passwordEncoder; this.jwtTokenService=jwtTokenService;
    }
    @Transactional
    public PatientResponse registerPatient(RegisterPatientRequest request) {
        String normalizedEmail=request.email().trim().toLowerCase();
        if(userRepository.existsByEmailIgnoreCase(normalizedEmail)) throw new DuplicateResourceException("An account already exists with that email.");
        UserEntity user=userRepository.save(new UserEntity(normalizedEmail,passwordEncoder.encode(request.password()),Role.ROLE_PATIENT));
        PatientEntity patient=new PatientEntity(user,request.firstName().trim(),request.lastName().trim(),request.birthDate(),request.phone(),request.address());
        return patientMapper.toResponse(patientRepository.save(patient));
    }
    @Transactional(readOnly=true)
    public LoginResponse login(LoginRequest request) {
        UserEntity user=userRepository.findByEmailIgnoreCase(request.email().trim()).filter(UserEntity::isEnabled).orElseThrow(() -> new BadCredentialsException("Invalid email or password."));
        if(!passwordEncoder.matches(request.password(),user.getPasswordHash())) throw new BadCredentialsException("Invalid email or password.");
        JwtTokenService.IssuedToken issued=jwtTokenService.issue(user);
        return new LoginResponse(issued.value(),"Bearer",issued.expiresAt(),user.getRole().name());
    }
}
