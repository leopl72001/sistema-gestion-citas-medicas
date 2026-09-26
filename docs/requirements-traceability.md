# Requirements traceability

| Requirement / Rubric | Implementation |
|---|---|
| Stateless token security | `SecurityConfig`, OAuth2 Resource Server Bearer filter, `JwtTokenService` |
| HS512 JWT | `JwtConfig` + `JwtTokenService` |
| Token expiration rejection | Spring Security `JwtDecoder`; security entry point returns 401 for missing/invalid/expired token |
| `ROLE_PATIENT` ownership | `/patients/me`, `/appointments/me`, `/clinical-notes/me`; IDs derived from JWT subject |
| `ROLE_DOCTOR` assignment security | `AppointmentService.requireAssignedDoctor` |
| `ROLE_ADMIN` | doctor onboarding, office management, sensitive Actuator access |
| Overlap prevention | doctor-row `PESSIMISTIC_WRITE` lock + overlap query in one transaction |
| HTTP 409 conflict | `AppointmentConflictException` + `GlobalExceptionHandler` |
| >24h cancellation | `AppointmentPolicy` + `LateCancellationException` HTTP 400 |
| PostgreSQL persistence | Spring Data JPA prod profile |
| H2 dev | `application-dev.properties` |
| Mongo clinical history | `ClinicalNoteDocument` + Mongo repository |
| SQL + Mongo `@CreatedBy` | `AuditConfig`, `AuditableEntity`, `ClinicalNoteDocument` |
| OpenFDA `RestClient` | `OpenFdaDrugValidationService` + `RestClientConfig` |
| Circuit Breaker >50% / slow 1.5s | `ResilienceConfig` |
| External timeout 1.5s | `RestClientConfig` |
| Graceful fallback | `MANUAL_VALIDATION_REQUIRED` |
| Virtual Threads | `spring.threads.virtual.enabled=true` |
| Redis cache | `RedisCacheConfig` |
| doctor cache TTL | 10 minutes |
| drug cache TTL | 24 hours |
| Actuator health/metrics | Actuator dependency + exposure config |
| custom counters | `BusinessMetrics` |
| custom Actuator endpoint | `MediConnectEndpoint` |
| Records | all request/response DTOs are Java records |
| MapStruct | Patient, Doctor, Specialty, Office, Appointment, Clinical Note, Prescription mappers |
| RFC-style ProblemDetail | `GlobalExceptionHandler` |
| OpenAPI descriptions | controller `@Operation` annotations + `OpenApiConfig` |
| Docker Compose | PostgreSQL + MongoDB + Redis |
| Postman collection | `postman/MediConnect.postman_collection.json` |
| Unit tests | `AppointmentPolicyTest`, `AppointmentServiceTest` |
| Git history | repository commits / CI workflow |
