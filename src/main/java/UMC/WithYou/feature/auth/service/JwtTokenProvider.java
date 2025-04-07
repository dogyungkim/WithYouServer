package UMC.WithYou.feature.auth.service;

import UMC.WithYou.feature.auth.domain.RefreshToken;
import UMC.WithYou.feature.member.domain.Member;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider implements TokenProvider {
    @Value("${jwt.secret}")
    private String secret;
    private SecretKey key;
    @Value("${jwt.header}")
    private String jwtHeader;
    @Value("${jwt.prefix}")
    private String jwtTokenPrefix;
    @Value("${jwt.expiration-period}")
    private Long expirationPeriod;
    @Value("${jwt.refresh.expiration-period}")
    private Long refreshTokenValidTime;

    private final UserDetailsService userDetailsService;


    @PostConstruct
    private void init() {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Override
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.parsePayload(token));
        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    @Override
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(jwtHeader);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(jwtTokenPrefix)) {
            return bearerToken.substring(jwtTokenPrefix.length());
        }
        return null;
    }

    @Override
    public String createToken(String payload) {
        log.info("payload = {}" ,payload);
        Date now = new Date();
        Date validity = new Date(now.getTime() + TimeUnit.MINUTES.toMillis(expirationPeriod));
        return Jwts.builder()
                .setSubject(payload)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String parsePayload(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            //throw new AuthException(AuthExceptionType.INVALID_AUTHORIZATION);
            return null;
        }
    }

    @Override
    public RefreshToken createRefreshToken(String payload) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + TimeUnit.MINUTES.toMillis(expirationPeriod));

        String value = Jwts.builder()
                .setSubject(UUID.randomUUID().toString())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return RefreshToken.builder()
                .key(payload)
                .value(value)
                .expiredTime(refreshTokenValidTime)
                .build();
    }

    @Override
    public Boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            //throw new AuthException(AuthExceptionType.EXPIRED_AUTHORIZATION);
        } catch (JwtException | IllegalArgumentException e) {
            //throw new AuthException(AuthExceptionType.INVALID_AUTHORIZATION);
        }
        return true;
    }
}
