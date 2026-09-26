package com.mediconnect.office.api;
import jakarta.validation.constraints.*; public record OfficeRequest(@NotBlank @Size(max=120) String name,@NotBlank @Size(max=255) String address,@Size(max=50) String roomNumber,boolean active) {}
