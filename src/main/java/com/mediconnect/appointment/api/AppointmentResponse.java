package com.mediconnect.appointment.api;
import java.time.Instant; import java.util.UUID; import com.mediconnect.appointment.domain.AppointmentStatus;
public record AppointmentResponse(UUID id,UUID patientId,UUID doctorId,UUID officeId,Instant startTime,Instant endTime,AppointmentStatus status) {}
