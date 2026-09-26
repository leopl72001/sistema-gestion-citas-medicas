package com.mediconnect.appointment.infrastructure;
import com.mediconnect.appointment.api.AppointmentResponse; import com.mediconnect.appointment.domain.AppointmentEntity; import org.mapstruct.*;
@Mapper(componentModel="spring") public interface AppointmentMapper { @Mapping(target="patientId",source="patient.id") @Mapping(target="doctorId",source="doctor.id") @Mapping(target="officeId",source="office.id") AppointmentResponse toResponse(AppointmentEntity entity); }
