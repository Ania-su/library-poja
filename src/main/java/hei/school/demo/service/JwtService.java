package hei.school.demo.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  @Value("${jwt.secret}")
  private String secretKey;

  @Value("${jwt.expiration}")
  private Long tokenExpiration;

  private SecretKey getSecretKey() {
    return Keys.hmacShaKeyFor(secretKey.getBytes());
  }

  public String generateToken(UserDetails user) 
  {
    String role = user.getAuthorities().stream()
        .findFirst()
        .map(r -> r.toString())
        .orElse(null);

    return Jwts.builder()
        .subject(user.getUsername())
        .claim("role", role)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + tokenExpiration))
        .signWith(getSecretKey())
        .compact();
  }

  public boolean isValid(String token, UserDetails user) {
    String userEmail = extractUsername(token);
    return userEmail != null && userEmail.equals(user.getUsername()) && !isTokenExpired(token);
  }

  public String extractUsername(String token) {
    return Jwts.parser()
        .verifyWith(getSecretKey())
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }

    public String extractRole(String token) {

    return Jwts.parser()
        .verifyWith(getSecretKey())
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .get("role", String.class);
  }

  public boolean isTokenExpired(String token) {
    Date expiration =
        Jwts.parser()
            .verifyWith(getSecretKey())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getExpiration();
    return expiration != null && expiration.before(new Date());
  }
}
