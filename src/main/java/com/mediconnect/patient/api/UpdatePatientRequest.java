package com.mediconnect.patient.api;
import java.time.LocalDate; import jakarta.validation.constraints.*;
public record UpdatePatientRequest(@NotBlank @Size(max=80) String firstName,@NotBlank @Size(max=80) String lastName,@NotNull @Past LocalDate birthDate,@Size(max=30) String phone,@Size(max=255) String address) {}
