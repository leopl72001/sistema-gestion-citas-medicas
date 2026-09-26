package com.mediconnect.doctor.infrastructure;
import com.mediconnect.doctor.api.DoctorResponse; import com.mediconnect.doctor.domain.DoctorEntity; import org.mapstruct.*;
@Mapper(componentModel="spring") public interface DoctorMapper { @Mapping(target="email",source="user.email") @Mapping(target="specialtyId",source="specialty.id") @Mapping(target="specialtyName",source="specialty.name") DoctorResponse toResponse(DoctorEntity entity); }
