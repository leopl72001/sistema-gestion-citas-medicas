package com.mediconnect.patient.api;
import java.time.LocalDate; import java.util.UUID;
public record PatientResponse(UUID id,String email,String firstName,String lastName,LocalDate birthDate,String phone,String address) {}
