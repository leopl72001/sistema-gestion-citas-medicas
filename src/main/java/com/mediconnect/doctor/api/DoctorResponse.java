package com.mediconnect.doctor.api;
import java.io.Serializable; import java.util.UUID;
public record DoctorResponse(UUID id,String email,String firstName,String lastName,String medicalLicense,UUID specialtyId,String specialtyName,boolean active) implements Serializable {}
