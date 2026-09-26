package com.mediconnect.auth.api;

import com.mediconnect.auth.application.AuthService;
import com.mediconnect.patient.api.PatientResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) { this.authService = authService; }
    @PostMapping("/register") @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a patient", description = "Public endpoint that creates a new ROLE_PATIENT account and patient profile.")
    public PatientResponse register(@Valid @RequestBody RegisterPatientRequest request) { return authService.registerPatient(request); }
    @PostMapping("/login")
    @Operation(summary = "Authenticate", description = "Validates credentials and returns an HS512-signed stateless JWT containing role claims.")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) { return authService.login(request); }
}
