package leesh.devcom.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import leesh.devcom.backend.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static leesh.devcom.backend.exception.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtUtil implements InitializingBean {

    public static final String AUTHORITIES_KEY = "auth";
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    public static final Long ACCESS_TOKEN_EXPIRED_SEC = 60L;
    public static final Long REFRESH_TOKEN_EXPIRED_SEC = 2 * 60L;
    public static final String ISSUER = "devcom";
    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public String createAccessToken(@NotNull UserDetails userDetails) {

        Instant issuedAt = Instant.now();
        Instant expiredAt = issuedAt.plus(ACCESS_TOKEN_EXPIRED_SEC, ChronoUnit.SECONDS);

        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setIssuer(ISSUER)
                .setIssuedAt(Date.from(issuedAt))
                .setExpiration(Date.from(expiredAt))
                .setSubject(userDetails.getUsername())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(@NotNull String accessToken) {

        Instant issuedAt = Instant.now();
        Instant expiredAt = issuedAt.plus(REFRESH_TOKEN_EXPIRED_SEC, ChronoUnit.SECONDS);

        String subject = getClaims(accessToken).getSubject();

        String refreshToken = Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setIssuer(ISSUER)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(expiredAt))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // set redis data
        redisTemplate.opsForValue().set(refreshToken, subject, REFRESH_TOKEN_EXPIRED_SEC, TimeUnit.SECONDS);
        return refreshToken;
    }

    public Authentication getAuthentication(String accessToken) {

        Claims claims = getClaims(accessToken);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new UserAuthentication(claims.getSubject(), "", authorities);
    }

    private Claims getClaims(String jwt) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    public boolean validate(String jwt) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new CustomException(INVALID_JWT);
        } catch (ExpiredJwtException e) {
            throw new CustomException(EXPIRED_ACCESS_TOKEN);
        } catch (UnsupportedJwtException | IllegalArgumentException e) {
            throw new CustomException(INTERNAL_SERVER_ERROR);
        }
    }
}
