# MediConnect Enterprise Pro

Backend Java 21 / Spring Boot 4.1.1 para gestión de citas médicas, persistencia híbrida de historia clínica y validación resiliente de medicamentos.

El proyecto está organizado para cubrir la rúbrica del Módulo 2: seguridad stateless con JWT HS512, roles y `@PreAuthorize`, PostgreSQL + MongoDB, Redis, Virtual Threads, Resilience4j, Actuator, Records, MapStruct, ProblemDetail, Swagger/OpenAPI, Docker Compose, pruebas y colección Postman.

## Arquitectura

```text
Cliente / Postman / Swagger
          |
          v
Spring MVC REST API
          |
          +--> Spring Security 7 + JWT HS512
          |
          +--> PostgreSQL / H2
          |      users, patients, doctors, specialties,
          |      offices, appointments, prescriptions
          |
          +--> MongoDB
          |      clinical_notes
          |
          +--> Redis
          |      doctorsBySpecialty, validatedDrugs
          |
          +--> OpenFDA RestClient
                 Resilience4j Circuit Breaker + fallback
```

La estructura es **package-by-feature**, evitando concentrar toda la lógica en controladores globales.

## Stack

- Java 21
- Spring Boot 4.1.1
- Spring Security / OAuth2 Resource Server
- Spring MVC + `RestClient`
- Spring Data JPA
- PostgreSQL
- H2 en desarrollo
- Spring Data MongoDB
- Spring Data Redis / Spring Cache
- Resilience4j 2.4.0
- MapStruct 1.6.3
- Spring Boot Actuator + Micrometer
- springdoc-openapi 3.1.1
- JUnit 5 + Mockito
- Docker Compose

## Seguridad

La API es stateless. HTTP Basic y form login están deshabilitados.

Roles:

- `ROLE_PATIENT`: administra únicamente su perfil, sus citas y su historial clínico.
- `ROLE_DOCTOR`: consulta su agenda, completa citas, escribe notas y crea prescripciones solamente para citas asignadas.
- `ROLE_ADMIN`: crea doctores, administra consultorios y accede a endpoints sensibles de Actuator.

JWT:

- endpoint: `POST /api/v1/auth/login`
- algoritmo: HMAC SHA-512
- claims: `sub`, `userId`, `roles`, `iat`, `exp`
- secreto mediante `JWT_SECRET`
- mínimo 64 bytes para HS512

Rutas públicas:

```text
POST /api/v1/auth/register
POST /api/v1/auth/login
GET  /api/v1/specialties
GET  /actuator/health
GET  /actuator/info
/swagger-ui/**
/v3/api-docs/**
```

## Perfiles

### DEV

- H2 in-memory en modo PostgreSQL
- MongoDB local/container
- caché simple en memoria
- SQL DEBUG
- Hibernate `create-drop`

### PROD

- PostgreSQL + HikariCP
- MongoDB
- Redis
- secretos mediante variables de entorno

> Para una producción regulada real, `ddl-auto=update` debe reemplazarse por migraciones versionadas con Flyway o Liquibase.

## Instalación local

Requisitos:

- JDK 21
- Maven 3.9+
- Docker Desktop
- Git

Opcional para visualizar datos:

- DBeaver: PostgreSQL
- MongoDB Compass: MongoDB
- RedisInsight: Redis

### 1. Clonar

```bash
git clone https://github.com/leopl72001/sistema-gestion-citas-medicas.git
cd sistema-gestion-citas-medicas
```

### 2. Crear `.env`

PowerShell:

```powershell
Copy-Item .env.example .env
```

Linux/macOS:

```bash
cp .env.example .env
```

Cambia los valores `change-me` y define un `JWT_SECRET` de al menos 64 caracteres. `.env` está excluido de Git.

### 3. Levantar infraestructura

```bash
docker compose up -d
docker compose ps
```

Servicios:

```text
PostgreSQL  localhost:5432
MongoDB     localhost:27017
Redis       localhost:6379
```

### 4. Ejecutar DEV

```bash
mvn spring-boot:run
```

DEV usa H2 para SQL, pero MongoDB sigue disponible para las notas clínicas.

### 5. Ejecutar perfil PROD local

En `.env`:

```properties
SPRING_PROFILES_ACTIVE=prod
DB_URL=jdbc:postgresql://localhost:5432/mediconnect
DB_USERNAME=mediconnect
DB_PASSWORD=YOUR_PASSWORD
MONGO_URI=mongodb://mediconnect:YOUR_PASSWORD@localhost:27017/mediconnect?authSource=admin
REDIS_HOST=localhost
REDIS_PORT=6379
JWT_SECRET=YOUR_64_PLUS_CHARACTER_SECRET
```

Luego:

```bash
mvn spring-boot:run
```

## Admin de desarrollo

No existe registro público de administradores.

Para crear uno en DEV, define:

```properties
DEV_ADMIN_EMAIL=admin@mediconnect.local
DEV_ADMIN_PASSWORD=ChangeThisAdminPassword123!
```

Al iniciar, la contraseña se almacena con BCrypt.

## Flujo básico

### Especialidades

```http
GET /api/v1/specialties
```

### Login

```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "admin@mediconnect.local",
  "password": "ChangeThisAdminPassword123!"
}
```

### Crear consultorio

```http
POST /api/v1/admin/offices
Authorization: Bearer <admin-token>
Content-Type: application/json

{
  "name": "Consultorio Norte",
  "address": "Carrera 53 # 80-67",
  "roomNumber": "301",
  "active": true
}
```

### Crear doctor

```http
POST /api/v1/admin/doctors
Authorization: Bearer <admin-token>
Content-Type: application/json

{
  "email": "doctor@mediconnect.local",
  "temporaryPassword": "DoctorPassword123!",
  "firstName": "Laura",
  "lastName": "Gomez",
  "medicalLicense": "RM-10001",
  "specialtyId": "<specialty-uuid>"
}
```

### Registrar paciente

```http
POST /api/v1/auth/register
Content-Type: application/json

{
  "email": "patient@mediconnect.local",
  "password": "PatientPassword123!",
  "firstName": "Daniel",
  "lastName": "Perez",
  "birthDate": "1994-05-11",
  "phone": "+573001112233",
  "address": "Barranquilla"
}
```

### Agendar cita

```http
POST /api/v1/appointments
Authorization: Bearer <patient-token>
Content-Type: application/json

{
  "doctorId": "<doctor-uuid>",
  "officeId": "<office-uuid>",
  "startTime": "2026-10-15T15:00:00Z",
  "endTime": "2026-10-15T15:30:00Z"
}
```

## Protección contra citas duplicadas

La reserva no usa un simple `exists -> save` vulnerable a condiciones de carrera.

Dentro de una misma transacción SQL:

1. se bloquea la fila del doctor con `PESSIMISTIC_WRITE`;
2. se ejecuta el chequeo de solapamiento;
3. solo se inserta si el intervalo está libre.

Predicado:

```text
existing.start < requested.end
AND existing.end > requested.start
AND existing.status != CANCELLED
```

Si hay conflicto se lanza `AppointmentConflictException` y se responde `409 Conflict` mediante `ProblemDetail`.

## Regla de cancelación

El paciente solo puede cancelar autónomamente cuando faltan **más de 24 horas**.

A exactamente 24 horas o menos:

- `LateCancellationException`
- HTTP `400`
- incremento de `mediconnect.late_cancellations`

## MongoDB: notas de evolución

Doctor:

```http
POST /api/v1/appointments/{appointmentId}/clinical-notes
```

Antes de guardar, se valida que el doctor autenticado sea el asignado a la cita.

Paciente:

```http
GET /api/v1/clinical-notes/me
```

El paciente no envía un `patientId`; el sistema lo resuelve desde el JWT y filtra MongoDB por su UUID interno.

Los documentos guardan `createdAt` y `createdBy` automáticamente.

## OpenFDA + Circuit Breaker

```http
POST /api/v1/appointments/{appointmentId}/prescriptions
```

Configuración principal:

- failure rate: 50%
- slow-call rate: 50%
- slow call: 1.5 s
- connect/read timeout: 1.5 s
- sliding window: 10
- mínimo antes de evaluar: 5
- open state: 10 s
- half-open calls: 3

Si OpenFDA falla, la API continúa y marca:

```text
MANUAL_VALIDATION_REQUIRED
```

No se devuelve 500 por la caída del proveedor. También se incrementa:

```text
mediconnect.circuit_breaker_fallbacks
```

Los fallbacks no se cachean.

## Redis

TTL en PROD:

```text
doctorsBySpecialty  10 minutos
validatedDrugs      24 horas
```

DEV usa caché en memoria.

## Virtual Threads

```properties
spring.threads.virtual.enabled=true
```

El endpoint personalizado de Actuator permite demostrarlo:

```http
GET /actuator/mediconnect
Authorization: Bearer <admin-token>
```

Respuesta esperada:

```json
{
  "application": "MediConnect Enterprise Pro",
  "openFdaCircuitBreaker": "CLOSED",
  "requestHandledByVirtualThread": true
}
```

## Actuator

Públicos:

```text
/actuator/health
/actuator/info
```

ADMIN:

```text
/actuator/metrics
/actuator/mediconnect
```

En PROD, los health contributors permiten verificar SQL, MongoDB y Redis.

## ProblemDetail

Ejemplo de conflicto:

```json
{
  "type": "https://mediconnect.local/problems/appointment-conflict",
  "title": "Appointment conflict",
  "status": 409,
  "detail": "The doctor already has an appointment that overlaps this time window."
}
```

## Swagger

```text
http://localhost:8080/swagger-ui.html
http://localhost:8080/v3/api-docs
```

## Tests

```bash
mvn test
```

Incluye pruebas para:

- ventanas de cita inválidas;
- citas futuras válidas;
- cancelación exactamente a 24 horas;
- cancelación con más de 24 horas;
- rechazo de solapamiento antes de persistir.

GitHub Actions ejecuta la suite Maven en pushes y pull requests a `main`.

## Postman

Importar:

```text
postman/MediConnect.postman_collection.json
```

Incluye variables para Bearer tokens e IDs dinámicos y flujos de autenticación, seguridad, citas, notas, prescripciones y observabilidad.

## Inspección de bases de datos

### PostgreSQL / DBeaver

```text
Host: localhost
Port: 5432
Database: mediconnect
Username: POSTGRES_USER
Password: POSTGRES_PASSWORD
```

Tablas principales:

```text
app_users
patients
doctors
specialties
offices
appointments
prescriptions
prescription_items
```

### MongoDB Compass

Usa `MONGO_URI` y revisa:

```text
clinical_notes
```

### RedisInsight

```text
localhost:6379
```

## Sustentación de 5 minutos

1. Login como paciente y acceso a endpoint ADMIN -> `403`.
2. Login admin y creación de doctor/consultorio.
3. Agendar una cita.
4. Intentar una cita solapada -> `409`.
5. Crear nota y mostrarla en MongoDB.
6. Simular caída de OpenFDA -> `MANUAL_VALIDATION_REQUIRED`, no 500.
7. Mostrar Actuator, métricas y Virtual Threads.

Ver también:

- `docs/requirements-traceability.md`
- `docs/video-script.md`
