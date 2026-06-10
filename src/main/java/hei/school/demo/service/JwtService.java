package hei.school.demo.service;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    private Long TOKEN_EXPIRATION = 360000L;

    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(UserDetails user) {
        return Jwts.builder()
            .subject(user.getUsername())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
            .signWith(getSecretKey())
            .compact();
    }

    public boolean isValid(String token, UserDetails user){
        String userEmail = extractUsername(token);
        return userEmail.equals(user.getUsername()) && !isTokenExpired(token);
    }


    public String extractUsername(String token) {
        return Jwts.parser()
            .verifyWith(getSecretKey()).build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

    public boolean isTokenExpired(String token){
        Date expiration = Jwts.parser()
            .verifyWith(getSecretKey()).build()
            .parseSignedClaims(token)
            .getPayload()
            .getExpiration();
        return expiration != null && expiration.before(new Date());
    }

}