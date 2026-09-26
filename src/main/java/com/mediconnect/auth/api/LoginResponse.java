package com.mediconnect.auth.api;
import java.time.Instant;
public record LoginResponse(String token, String tokenType, Instant expiresAt, String role) {}
