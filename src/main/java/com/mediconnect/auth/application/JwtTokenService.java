package com.mediconnect.auth.application;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import com.mediconnect.auth.domain.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService {
    private final JwtEncoder jwtEncoder;
    private final long expirationMinutes;
    public JwtTokenService(JwtEncoder jwtEncoder,@Value("${app.security.jwt-expiration-minutes}") long expirationMinutes){this.jwtEncoder=jwtEncoder;this.expirationMinutes=expirationMinutes;}
    public IssuedToken issue(UserEntity user){
        Instant issuedAt=Instant.now(); Instant expiresAt=issuedAt.plus(expirationMinutes, ChronoUnit.MINUTES);
        JwtClaimsSet claims=JwtClaimsSet.builder().issuer("mediconnect").issuedAt(issuedAt).expiresAt(expiresAt).subject(user.getEmail()).claim("userId",user.getId().toString()).claim("roles", List.of(user.getRole().name())).build();
        JwsHeader header=JwsHeader.with(MacAlgorithm.HS512).type("JWT").build();
        String token=jwtEncoder.encode(JwtEncoderParameters.from(header,claims)).getTokenValue();
        return new IssuedToken(token,expiresAt);
    }
    public record IssuedToken(String value, Instant expiresAt) {}
}
