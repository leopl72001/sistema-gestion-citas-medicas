package com.mediconnect.patient.api;

import com.mediconnect.patient.application.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/v1/patients") @Tag(name="Patients") @SecurityRequirement(name="bearerAuth")
public class PatientController {
    private final PatientService patientService;
    public PatientController(PatientService patientService){this.patientService=patientService;}
    @GetMapping("/me") @Operation(summary="Get my patient profile",description="Returns only the profile owned by the authenticated patient.")
    public PatientResponse me(@AuthenticationPrincipal Jwt jwt){return patientService.getCurrentPatient(jwt.getSubject());}
    @PatchMapping("/me") @Operation(summary="Update my patient profile",description="Updates demographic data for the authenticated patient only.")
    public PatientResponse updateMe(@AuthenticationPrincipal Jwt jwt,@Valid @RequestBody UpdatePatientRequest request){return patientService.updateCurrentPatient(jwt.getSubject(),request);}
}
