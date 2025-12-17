package com.common.auth.security;

import com.common.auth.entity.Role;
import com.common.auth.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long accessTtlSeconds;
    private final long refreshTtlSeconds;
    private final String issuer;

    public JwtService(
            @Value("${security.jwt.secret}") String secretKey,
            @Value("${security.jwt.access-ttl-seconds}") long accessTtlSeconds,
            @Value("${security.jwt.refresh-ttl-seconds}") long refreshTtlSeconds,
            @Value("${security.jwt.issuer}") String issuer
    ){
        if(secretKey == null || secretKey.length() < 64 )
        {
            throw new IllegalArgumentException("Invalid Secret: "+secretKey);
        }
        this.secretKey= Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTtlSeconds=accessTtlSeconds;
        this.refreshTtlSeconds=refreshTtlSeconds;
        this.issuer=issuer;
    }

    // Generating access token
    public String generateAccessToken(User user)
    {
        Instant now= Instant.now();

        List<String> roles = user.getRoles() == null ? List.of() :
                user.getRoles().stream().map(Role::getName).toList();

        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(user.getId().toString())
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(accessTtlSeconds)))
                .claims(Map.of(
                        "email", user.getEmail(),
                        "roles", roles,
                        "typ", "access"
                ))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    // Generating refresh token
    public String generateRefreshToken(User user, String jti)
    {
        Instant now= Instant.now();

        return Jwts.builder()
                .id(jti)
                .subject(user.getId().toString())
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(accessTtlSeconds)))
                .claim("typ", "refresh")
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    // Parse the token
    public Jws<Claims> parseToken(String token)
    {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
    }

    // Checking if the token is access token or not.
    public boolean isAccessToken(String token)
    {
        Claims claims = parseToken(token).getPayload();

        return "access".equals(claims.get("typ"));
    }

    // Checking if the token is refresh token or not.
    public boolean isRefreshToken(String token)
    {
        Claims claims = parseToken(token).getPayload();

        return "refresh".equals(claims.get("typ"));
    }

    // Fetching the user id from token
    public UUID getUserId(String token)
    {
        Claims claims = parseToken(token).getPayload();

        return UUID.fromString(claims.getSubject());
    }

    // Fetching the token id
    public String getJti(String token)
    {
        return parseToken(token).getPayload().getId();
    }

    // Fetching the roles
    public List<String> getRoles(String token)
    {
        Claims payload = parseToken(token).getPayload();

        return (List<String>) payload.get("roles");
    }

    // Fetching the email
    public String getEmail(String token)
    {
        Claims payload = parseToken(token).getPayload();

        return (String) payload.get("email");
    }

}
