package com.mediconnect.specialty.infrastructure;
import java.util.Optional; import java.util.UUID; import com.mediconnect.specialty.domain.SpecialtyEntity; import org.springframework.data.jpa.repository.JpaRepository;
public interface SpecialtyRepository extends JpaRepository<SpecialtyEntity,UUID>{Optional<SpecialtyEntity> findByNameIgnoreCase(String name);}
