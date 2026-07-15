package com.huangpi.platform.security;

import com.huangpi.platform.config.AppProperties;
import com.huangpi.platform.domain.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final AppProperties properties;
    private final SecretKey secretKey;

    public JwtService(AppProperties properties) {
        this.properties = properties;
        this.secretKey = Keys.hmacShaKeyFor(properties.jwt().secret().getBytes(StandardCharsets.UTF_8));
    }

    public IssuedToken issue(UserEntity user, Long merchantId) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(properties.jwt().expiration());
        String jti = UUID.randomUUID().toString();
        var builder = Jwts.builder()
                .subject(user.getId().toString())
                .id(jti)
                .claim("role", user.getRole().name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt));
        if (merchantId != null) {
            builder.claim("merchantId", merchantId);
        }
        String token = builder.signWith(secretKey).compact();
        return new IssuedToken(token, jti, expiresAt);
    }

    public SessionPrincipal parse(String token) {
        Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
        Long userId = Long.valueOf(claims.getSubject());
        String roleValue = claims.get("role", String.class);
        Number merchantIdValue = claims.get("merchantId", Number.class);
        Long merchantId = merchantIdValue == null ? null : merchantIdValue.longValue();
        return new SessionPrincipal(
                userId,
                com.huangpi.platform.domain.UserRole.valueOf(roleValue),
                merchantId,
                claims.getId(),
                claims.getExpiration().toInstant());
    }

    public record IssuedToken(String token, String jti, Instant expiresAt) {
    }
}
