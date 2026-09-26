package com.mediconnect.appointment.api;

import java.util.List;
import java.util.UUID;
import com.mediconnect.appointment.application.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/v1/appointments") @Tag(name="Appointments") @SecurityRequirement(name="bearerAuth")
public class AppointmentController {
 private final AppointmentService service;
 public AppointmentController(AppointmentService service){this.service=service;}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) @Operation(summary="Book my appointment",description="PATIENT-only. Uses a transactional doctor-row lock plus an overlap query to prevent double booking.") public AppointmentResponse create(@AuthenticationPrincipal Jwt jwt,@Valid @RequestBody CreateAppointmentRequest request){return service.createForPatient(jwt.getSubject(),request);}
 @GetMapping("/me") @Operation(summary="List my appointments",description="PATIENT-only and scoped to the authenticated patient's identity.") public List<AppointmentResponse> mine(@AuthenticationPrincipal Jwt jwt){return service.listForPatient(jwt.getSubject());}
 @DeleteMapping("/{id}") @Operation(summary="Cancel my appointment",description="PATIENT-only. Autonomous cancellation is rejected when 24 hours or less remain.") public AppointmentResponse cancel(@AuthenticationPrincipal Jwt jwt,@PathVariable UUID id){return service.cancelForPatient(jwt.getSubject(),id);}
 @GetMapping("/doctor/today") @Operation(summary="Doctor daily agenda",description="DOCTOR-only. Returns appointments assigned to the authenticated doctor for the current day.") public List<AppointmentResponse> doctorToday(@AuthenticationPrincipal Jwt jwt){return service.todayForDoctor(jwt.getSubject());}
 @PatchMapping("/{id}/complete") @Operation(summary="Complete appointment",description="DOCTOR-only. The appointment must belong to the authenticated doctor.") public AppointmentResponse complete(@AuthenticationPrincipal Jwt jwt,@PathVariable UUID id){return service.completeForDoctor(jwt.getSubject(),id);}
}
