package jpa.board.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jpa.board.domain.RefreshToken;
import jpa.board.domain.RoleType;
import jpa.board.dto.JwtTokenResponse;
import jpa.board.repository.RefreshTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {
    private final Key key;
    private final long accessTokenExpireTime;
    private final long refreshTokenExpireTime;
    private final String tokenHeader;
    private final String tokenHeaderPrefix;
    private final String authoritiesKey;
    private final String redisHash;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-token-expire-time}") long accessTokenExpireTime,
            @Value("${jwt.refresh-token-expire-time}") long refreshTokenExpireTime,
            @Value("${jwt.token-header}") String tokenHeader,
            @Value("${jwt.token-header-prefix}") String tokenHeaderPrefix,
            @Value("${jwt.token-authorities-key}") String authoritiesKey,
            @Value("${spring.redis.refresh_token_redis_hash}") String redisHash,
            RefreshTokenRepository refreshTokenRepository,
            RedisTemplate<String, Object> redisTemplate
    ) {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(bytes);
        this.accessTokenExpireTime = accessTokenExpireTime;
        this.refreshTokenExpireTime = refreshTokenExpireTime;
        this.tokenHeader = tokenHeader;
        this.tokenHeaderPrefix = tokenHeaderPrefix;
        this.authoritiesKey = authoritiesKey;
        this.redisHash = redisHash;
        this.refreshTokenRepository = refreshTokenRepository;
        this.redisTemplate = redisTemplate;
    }

    public JwtTokenResponse generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenExpireTime = now.plusSeconds(accessTokenExpireTime);
        String accessToken =  Jwts.builder()
                .setSubject(authentication.getName())
                .claim(authoritiesKey, authorities)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenExpireTime.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(now.plusSeconds(refreshTokenExpireTime).toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        refreshTokenRepository.save(RefreshToken.builder()
                                        .userEmail(authentication.getName())
                                        .token(refreshToken)
                                        .expiration(refreshTokenExpireTime)
                                        .build());

        return JwtTokenResponse.builder()
                .grantType(tokenHeaderPrefix)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String resolveToken(HttpServletRequest request) {
        String token = "";
        try {
            token = request.getHeader(tokenHeader).substring(7);
        }catch (NullPointerException exception) {
            log.info("header is not valid " + request.getHeader(tokenHeader));
        }
        return token;
    }

    public String getMemberEmail(String refreshToken) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(refreshToken)
                .getBody()
                .getSubject();
    }

    public boolean validateRefreshToken(String email, String refreshToken) {
        String storedRefreshToken = refreshTokenRepository.findByUserEmail(email).getToken();

        if (StringUtils.isNotBlank(storedRefreshToken) && !storedRefreshToken.equals(refreshToken)) {
            log.info("Stored Refresh Token Not Match");
            return false;
        }

        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(refreshToken);
            return true;
        }catch (ExpiredJwtException e) {
            log.info("Expired JWT Refresh Token", e);
        }
        return false;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        }catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        }catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        }catch (IllegalArgumentException e) {
            log.info("JWT claims String is Empty", e);
        }
        return false;
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get(authoritiesKey) == null) {
            throw new RuntimeException("No Authentication");
        }
        Collection<? extends GrantedAuthority> authorities
                = Arrays.stream(claims.get("role").toString().split(","))
                .map(role -> new SimpleGrantedAuthority(RoleType.valueOf(role).name()))
                .collect(Collectors.toList());
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        }catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public void invalidRefreshToken(String memberEmail) {
        String key = redisHash + memberEmail;
        if (redisTemplate.hasKey(key)){
            redisTemplate.delete(key);
        }
    }
}
