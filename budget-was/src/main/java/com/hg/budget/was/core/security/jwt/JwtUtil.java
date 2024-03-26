package com.hg.budget.was.core.security.jwt;

import com.hg.budget.was.core.exception.AuthenticationException;
import com.hg.budget.was.core.security.UserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

public class JwtUtil {

    private static final String SECRET_KEY = "christmas";
    private static final Long TOKEN_EXPIRATION = 15 * 60_000L;

    public UserDetails parseToken(String accessToken) {
        try {
            final String secretKey = twistSecretKey();
            final Claims body = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(accessToken)
                .getBody();

            final String userId = body.getSubject();
            final String role = (String) body.get("role");

            return new UserDetails(userId, role);
        } catch (JwtException e) {
            throw new AuthenticationException(e);
        }
    }

    public String issueToken(UserDetails userDetails) {
        final Date now = new Date();
        final Claims claims = Jwts.claims();
        claims.setSubject(userDetails.getId());
        claims.put("role", userDetails.getRole());

        final String secretKey = twistSecretKey();

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + TOKEN_EXPIRATION))
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
            .compact();
    }

    private String twistSecretKey() {
        String secretKey = SECRET_KEY;
        for (int i = 0; i < 5; i++) {
            secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        }

        return secretKey;
    }
}
