package com.mediconnect.doctor.api;
import java.util.UUID; import jakarta.validation.constraints.*;
public record CreateDoctorRequest(@NotBlank @Email String email,@NotBlank @Size(min=8,max=100) String temporaryPassword,@NotBlank @Size(max=80) String firstName,@NotBlank @Size(max=80) String lastName,@NotBlank @Size(max=80) String medicalLicense,@NotNull UUID specialtyId) {}
