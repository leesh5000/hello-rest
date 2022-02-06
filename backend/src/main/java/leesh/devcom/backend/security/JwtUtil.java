package leesh.devcom.backend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import leesh.devcom.backend.common.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtUtil implements InitializingBean {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisUtil redisUtil;

    public static final Long ACCESS_TOKEN_EXPIRED_SEC = 60L;
    public static final Long REFRESH_TOKEN_EXPIRED_SEC = 2 * 60L;
    public static final String ISSUER = "devcom";
    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public String createAccessToken(Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", userDetails.getUsername());
        claims.put("authorities", userDetails.getAuthorities());

        Instant issuedAt = Instant.now();
        Instant expiredAt = issuedAt.plus(ACCESS_TOKEN_EXPIRED_SEC, ChronoUnit.SECONDS);

        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setIssuer(ISSUER)
                .setIssuedAt(Date.from(issuedAt))
                .setExpiration(Date.from(expiredAt))
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Instant issuedAt = Instant.now();
        Instant expiredAt = issuedAt.plus(REFRESH_TOKEN_EXPIRED_SEC, ChronoUnit.SECONDS);

        String refreshToken = Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setIssuer(ISSUER)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(expiredAt))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // set redis data
        redisUtil.set(refreshToken, userDetails, REFRESH_TOKEN_EXPIRED_SEC, TimeUnit.SECONDS);
        return refreshToken;
    }
}
