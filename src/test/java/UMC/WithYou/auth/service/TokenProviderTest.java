package UMC.WithYou.auth.service;

import UMC.WithYou.feature.auth.domain.RefreshToken;
import UMC.WithYou.feature.auth.service.JwtTokenProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TokenProviderTest {

    private JwtTokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        tokenProvider = new JwtTokenProvider();
        String secret = "this-is-a-secret-key-for-testing-purpose-and-it-is-long-enough";
        ReflectionTestUtils.setField(tokenProvider, "secret", secret);
        ReflectionTestUtils.setField(tokenProvider, "key", Keys.hmacShaKeyFor(secret.getBytes()));
        ReflectionTestUtils.setField(tokenProvider, "jwtHeader", "Authorization");
        ReflectionTestUtils.setField(tokenProvider, "jwtTokenPrefix", "Bearer ");
        ReflectionTestUtils.setField(tokenProvider, "expirationPeriod", 30L);
        ReflectionTestUtils.setField(tokenProvider, "refreshTokenValidTime", 1440L);
    }

    @Test
    void 토큰_생성_및_파싱_테스트() {
        String payload = "testuser";
        String token = tokenProvider.createToken(payload);

        assertNotNull(token);

        String parsedPayload = tokenProvider.parsePayload(token);
        assertEquals(payload, parsedPayload);
    }

    @Test
    void 유효한_토큰_검증_테스트() {
        String payload = "testuser";
        String token = tokenProvider.createToken(payload);

        assertTrue(tokenProvider.validateToken(token));
    }

    @Test
    void 만료된_토큰_검증_테스트() {
        String secret = "this-is-a-secret-key-for-testing-purpose-and-it-is-long-enough";
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        String expiredToken = Jwts.builder()
                .setSubject("testuser")
                .setIssuedAt(new Date(System.currentTimeMillis() - 10000))
                .setExpiration(new Date(System.currentTimeMillis() - 5000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // 만료 토큰은 false
        assertFalse(tokenProvider.validateToken(expiredToken));
    }

    @Test
    void 잘못된_토큰_검증_테스트() {
        String invalidToken = "invalid-token";
        // 잘못된 토큰은 false
        assertFalse(tokenProvider.validateToken(invalidToken));
    }


    @Test
    void 리프레시_토큰_생성_테스트() {
        String payload = "testuser";
        RefreshToken refreshToken = tokenProvider.createRefreshToken(payload);

        assertNotNull(refreshToken);
        assertEquals(payload, refreshToken.getKey());
        assertNotNull(refreshToken.getValue());
        // TTL 분 설정(1440분)을 초 단위로 저장하므로 1440*60
        assertEquals(1440L * 60, refreshToken.getExpiredTime());
    }

    @Test
    void 토큰_해석_테스트() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String token = "some-jwt-token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        String resolvedToken = tokenProvider.resolveToken(request);
        assertEquals(token, resolvedToken);
    }

    @Test
    void 토큰_해석_실패_헤더_없음_테스트() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn(null);

        String resolvedToken = tokenProvider.resolveToken(request);
        assertNull(resolvedToken);
    }

    @Test
    void 토큰_해석_실패_접두사_없음_테스트() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String token = "some-jwt-token";
        when(request.getHeader("Authorization")).thenReturn(token);

        String resolvedToken = tokenProvider.resolveToken(request);
        assertNull(resolvedToken);
    }
}