package com.vishvendra.procart.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenUtil {

  @Value("${JWT_SECRET_KEY:}")
  private String jwtSecret;
  @Value("${JWT_EXPIRATION_IN_MILLIS:3600000}")
  private Long jwtSecretExpirationInMillis;
  private SecretKey secretKey;

  public String generateToken(String username) {

    return Jwts.builder()
        .subject(username)
        .signWith(getSecretKey())
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + jwtSecretExpirationInMillis))
        .compact();
  }

  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parser()
          .verifyWith(getSecretKey())
          .build()
          .parseSignedClaims(authToken);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      log.error("JWT Token validation failed: {}", e.getMessage());
      return false;
    }
  }

  public String getUsernameFromJwtToken(String token) {
    Jws<Claims> claims = Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token);
    return claims.getPayload().getSubject();
  }

  private SecretKey getSecretKey() {
    if (this.secretKey == null) {
      this.secretKey = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HMACSHA512");
    }
    return this.secretKey;
  }

}
