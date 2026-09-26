package com.mediconnect.patient.infrastructure;
import com.mediconnect.patient.api.PatientResponse; import com.mediconnect.patient.domain.PatientEntity; import org.mapstruct.Mapper; import org.mapstruct.Mapping;
@Mapper(componentModel="spring") public interface PatientMapper { @Mapping(target="email",source="user.email") PatientResponse toResponse(PatientEntity patient); }
