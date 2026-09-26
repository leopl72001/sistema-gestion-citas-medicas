package com.mediconnect.auth.api;

import java.time.LocalDate;
import jakarta.validation.constraints.*;

public record RegisterPatientRequest(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8, max = 100) String password,
        @NotBlank @Size(max = 80) String firstName,
        @NotBlank @Size(max = 80) String lastName,
        @NotNull @Past LocalDate birthDate,
        @Size(max = 30) String phone,
        @Size(max = 255) String address) {}
