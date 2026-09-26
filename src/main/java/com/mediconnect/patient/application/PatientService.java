package com.mediconnect.patient.application;

import com.mediconnect.patient.api.*;
import com.mediconnect.patient.domain.PatientEntity;
import com.mediconnect.patient.infrastructure.PatientMapper;
import com.mediconnect.patient.infrastructure.PatientRepository;
import com.mediconnect.shared.exception.ResourceNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatientService {
    private final PatientRepository patientRepository; private final PatientMapper patientMapper;
    public PatientService(PatientRepository patientRepository,PatientMapper patientMapper){this.patientRepository=patientRepository;this.patientMapper=patientMapper;}
    @PreAuthorize("hasRole('PATIENT')") @Transactional(readOnly=true)
    public PatientResponse getCurrentPatient(String email){return patientMapper.toResponse(findByEmail(email));}
    @PreAuthorize("hasRole('PATIENT')") @Transactional
    public PatientResponse updateCurrentPatient(String email,UpdatePatientRequest request){PatientEntity patient=findByEmail(email);patient.updateProfile(request.firstName().trim(),request.lastName().trim(),request.birthDate(),request.phone(),request.address());return patientMapper.toResponse(patient);}
    public PatientEntity findEntityByEmail(String email){return findByEmail(email);}
    private PatientEntity findByEmail(String email){return patientRepository.findByUserEmailIgnoreCase(email).orElseThrow(() -> new ResourceNotFoundException("Patient profile not found."));}
}
