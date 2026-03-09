package com.example.demo.config;

import com.example.demo.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HexFormat;

@Service
public class JwtService {

    // Legge i valori da application.properties
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    // Genera la chiave di firma dal secret
    private SecretKey getSigningKey() {
        byte[] keyBytes = HexFormat.of().parseHex(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Genera il token JWT dall'utente
    public String generateToken(User user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusSeconds(expiration / 1000);

        return Jwts.builder()
                .subject(user.getEmail())           // chi è l'utente
                .claim("userId", user.getId())      // dati extra nel payload
                .claim("role", user.getRole().name())
                .issuedAt(toDate(now))              // quando è stato emesso
                .expiration(toDate(expiresAt))      // quando scade
                .signWith(getSigningKey())           // firma con il secret
                .compact();                         // genera la stringa finale
    }

    // Estrae l'email dal token
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    // Verifica se il token è ancora valido
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // Legge e verifica la firma del token
    // Se il token è manomesso o scaduto → lancia eccezione
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public LocalDateTime getExpirationDate() {
        return LocalDateTime.now().plusSeconds(expiration / 1000);
    }

    private Date toDate(LocalDateTime ldt) {
        return Date.from(ldt.atZone(ZoneId.of("UTC")).toInstant());
    }
}