package com.mediconnect.appointment.infrastructure;
import java.time.Instant; import java.util.*; import com.mediconnect.appointment.domain.*; import org.springframework.data.jpa.repository.*; import org.springframework.data.repository.query.Param;
public interface AppointmentRepository extends JpaRepository<AppointmentEntity,UUID>{ @Query("""
select (count(a) > 0) from AppointmentEntity a
where a.doctor.id = :doctorId
  and a.status <> :cancelledStatus
  and a.startTime < :endTime
  and a.endTime > :startTime
""") boolean existsDoctorOverlap(@Param("doctorId") UUID doctorId,@Param("startTime") Instant startTime,@Param("endTime") Instant endTime,@Param("cancelledStatus") AppointmentStatus cancelledStatus); List<AppointmentEntity> findByPatientIdOrderByStartTimeDesc(UUID patientId); List<AppointmentEntity> findByDoctorIdAndStartTimeGreaterThanEqualAndStartTimeLessThanOrderByStartTimeAsc(UUID doctorId,Instant startInclusive,Instant endExclusive); }
