package com.hamidspecial.medihive.auth.jwt;

import com.hamidspecial.medihive.auth.model.AuthUser;
import com.hamidspecial.medihive.exception.InvalidTokenException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.*;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JWTService {

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${token.expiration.time}")
    private long tokenExpirationTime;
    private Key key;
    private final RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        String token = Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * tokenExpirationTime))
                .signWith(key)
                .compact();
        redisTemplate.opsForValue().set(username, token, tokenExpirationTime, TimeUnit.MINUTES);
        return token;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        String storedToken = redisTemplate.opsForValue().get(username);
        return (username.equals(userDetails.getUsername())
                && storedToken != null
                && storedToken.equals(token)
                && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException("Token has expired.");
        } catch (MalformedJwtException e) {
            throw new InvalidTokenException("Invalid token format.");
        } catch (SignatureException e) {
            throw new InvalidTokenException("Invalid token signature.");
        } catch (JwtException e) {
            throw new InvalidTokenException("Invalid token.");
        }
    }

    public String generateTokenLinkKey(String username) {
        String token = generateToken(username);
        String resetLinkKey = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(resetLinkKey, token, 5, TimeUnit.MINUTES);
        return resetLinkKey;
    }
}

