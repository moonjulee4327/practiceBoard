package jpa.board.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jpa.board.domain.RoleType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final Key key;

    private final long expireTime;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expire-time}") long expireTime
    ) {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(bytes);
        this.expireTime = expireTime;
    }

    public String generateToken(Long memberId, RoleType role) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(memberId));
        claims.put("memberId", memberId);
        claims.put("role", role);
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenExpireTime = now.plusSeconds(expireTime);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenExpireTime.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
