package com.kb.kobo.user.security;

import com.kb.kobo.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class TokenProvider {

    private static final String USERNAME_CLAIM = "Username";

    private final Key key;
    private final long accessTokenValidityTime;

    public TokenProvider(@Value("${jwt.secret}") String secretKey,
                         @Value("${jwt.access-token-validity-in-milliseconds}") long accessTokenValidityTime) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenValidityTime = accessTokenValidityTime;
    }

    public String createAccessToken(User user) {
        long nowTime = (new Date().getTime());
        Date accessTokenExpiredTime = new Date(nowTime + accessTokenValidityTime);

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim(USERNAME_CLAIM, user.getUsername())
                .setExpiration(accessTokenExpiredTime)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get(USERNAME_CLAIM, String.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    public boolean validateToken(String token, String username) {
        String tokenUsername = extractUsername(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }
}
