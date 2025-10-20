package org.example.bookservice.configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtils {
    private static final String SECRET_KEY = "group13utc";
    private static final long EXPIRATION_TIME = 86400000; // 1 day

    public String generateToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    public String extractUsername(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .build()
                .verify(token)
                .getSubject();
    }

    public boolean isTokenValid(String token, String username) {
        return username.equals(extractUsername(token)) &&
                !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expiration = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .build()
                .verify(token)
                .getExpiresAt();
        return expiration.before(new Date());
    }
}
