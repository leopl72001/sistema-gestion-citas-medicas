# 5-minute presentation script

## 0:00-0:35 — Architecture

Show the repository and explain that transactional entities live in PostgreSQL/H2, clinical evolution notes live in MongoDB, and Redis stores distributed cache entries. Mention Java 21 Virtual Threads.

## 0:35-1:20 — JWT and roles

1. Login as a patient.
2. Call `POST /api/v1/admin/doctors` with the patient token.
3. Show `403 Forbidden`.
4. Login as admin and call the same endpoint successfully.

Mention that the app is stateless, HTTP Basic is disabled, JWT is HS512 and method-level security uses `@PreAuthorize`.

## 1:20-2:15 — Atomic appointments

1. Create a valid appointment.
2. From a second patient, attempt the same doctor's overlapping time.
3. Show HTTP `409` and the `ProblemDetail` payload.

Explain that the service locks the doctor row with `PESSIMISTIC_WRITE` before executing the overlap check, so two simultaneous requests cannot both pass the validation.

## 2:15-2:45 — 24-hour cancellation

Attempt to cancel an appointment inside the 24-hour window. Show HTTP `400`. Open `/actuator/metrics/mediconnect.late_cancellations` as admin.

## 2:45-3:25 — Hybrid persistence

As the assigned doctor, create a clinical note. Open MongoDB Compass and show the `clinical_notes` document with `appointmentId`, `patientId`, `doctorId`, `createdAt` and `createdBy`.

## 3:25-4:20 — Resilience

Restart with an invalid `OPENFDA_BASE_URL` (for example `http://localhost:9999`). Create a prescription. Show that the API still responds normally and the medication is marked `MANUAL_VALIDATION_REQUIRED` instead of causing HTTP 500.

Show `/actuator/mediconnect` and `/actuator/metrics/mediconnect.circuit_breaker_fallbacks`.

## 4:20-5:00 — Performance and quality

Show:

- `spring.threads.virtual.enabled=true`
- Redis cache configuration and TTLs
- `/actuator/mediconnect` reporting `requestHandledByVirtualThread`
- Swagger UI
- passing GitHub Actions CI / tests

Close with the test files that cover scheduling windows, overlap behavior and the 24-hour cancellation policy.
