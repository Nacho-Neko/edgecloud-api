package com.mikou.edgecloud.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;

public class JwtService {

    private final SecretKey key;
    private final Clock clock;
    private final long ttlSeconds;

    public JwtService(String secret, long ttlSeconds) {
        this(secret, ttlSeconds, Clock.systemUTC());
    }

    public JwtService(String secret, long ttlSeconds, Clock clock) {
        // For HS256, key length should be strong enough. Use a long random secret in config.
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.ttlSeconds = ttlSeconds;
        this.clock = clock;
    }

    public String createToken(String subject, String authoritiesCsv) {
        Instant now = clock.instant();
        Instant exp = now.plusSeconds(ttlSeconds);

        return Jwts.builder()
                .subject(subject)
                .claim("auth", authoritiesCsv)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public Jws<Claims> parseAndValidate(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .clock(() -> Date.from(clock.instant()))
                .build()
                .parseSignedClaims(token);
    }
}
