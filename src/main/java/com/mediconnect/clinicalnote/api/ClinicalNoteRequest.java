package com.mediconnect.clinicalnote.api; import jakarta.validation.constraints.*; public record ClinicalNoteRequest(@NotBlank @Size(max=10000) String content) {}
