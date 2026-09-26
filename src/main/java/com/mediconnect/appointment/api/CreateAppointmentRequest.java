package com.mediconnect.appointment.api;
import java.time.Instant; import java.util.UUID; import jakarta.validation.constraints.NotNull;
public record CreateAppointmentRequest(@NotNull UUID doctorId,@NotNull UUID officeId,@NotNull Instant startTime,@NotNull Instant endTime) {}
