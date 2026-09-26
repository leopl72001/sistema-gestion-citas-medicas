package com.mediconnect.appointment;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import java.time.Clock; import java.time.Instant; import java.time.ZoneOffset;
import com.mediconnect.appointment.application.AppointmentPolicy; import com.mediconnect.shared.exception.BusinessRuleException; import com.mediconnect.shared.exception.LateCancellationException; import org.junit.jupiter.api.Test;
class AppointmentPolicyTest { private final Instant now=Instant.parse("2026-09-26T17:00:00Z"); private final AppointmentPolicy policy=new AppointmentPolicy(Clock.fixed(now,ZoneOffset.UTC)); @Test void rejectsEndBeforeStart(){assertThatThrownBy(()->policy.validateBookingWindow(now.plusSeconds(7200),now.plusSeconds(3600))).isInstanceOf(BusinessRuleException.class);} @Test void acceptsValidFutureWindow(){assertThatCode(()->policy.validateBookingWindow(now.plusSeconds(7200),now.plusSeconds(9000))).doesNotThrowAnyException();} @Test void rejectsCancellationAtExactlyTwentyFourHours(){assertThatThrownBy(()->policy.validatePatientCancellation(now.plusSeconds(24*3600))).isInstanceOf(LateCancellationException.class);} @Test void allowsCancellationWhenMoreThanTwentyFourHoursRemain(){assertThatCode(()->policy.validatePatientCancellation(now.plusSeconds(25*3600))).doesNotThrowAnyException();} }
