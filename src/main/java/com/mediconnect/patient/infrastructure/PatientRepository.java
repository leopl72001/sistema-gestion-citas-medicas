package com.mediconnect.patient.infrastructure;
import java.util.Optional; import java.util.UUID; import com.mediconnect.patient.domain.PatientEntity; import org.springframework.data.jpa.repository.JpaRepository;
public interface PatientRepository extends JpaRepository<PatientEntity,UUID>{ Optional<PatientEntity> findByUserEmailIgnoreCase(String email); }
