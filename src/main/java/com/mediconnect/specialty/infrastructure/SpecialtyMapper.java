package com.mediconnect.specialty.infrastructure;
import com.mediconnect.specialty.api.SpecialtyResponse; import com.mediconnect.specialty.domain.SpecialtyEntity; import org.mapstruct.Mapper;
@Mapper(componentModel="spring") public interface SpecialtyMapper { SpecialtyResponse toResponse(SpecialtyEntity entity); }
