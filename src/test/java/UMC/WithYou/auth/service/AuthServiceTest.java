package UMC.WithYou.auth.service;

import UMC.WithYou.feature.auth.controller.LoginResponse;
import UMC.WithYou.feature.auth.domain.RefreshToken;
import UMC.WithYou.feature.auth.repository.RefreshTokenRepository;
import UMC.WithYou.feature.auth.service.AuthService;
import UMC.WithYou.feature.auth.service.TokenProvider;
import UMC.WithYou.feature.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private TokenProvider tokenProvider;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("refreshAccessToken 성공 - 토큰 로테이션")
    void refreshAccessToken_success() {
        // given
        String oldRefreshValue = "old-refresh";
        String identifier = "user-123";

        RefreshToken existing = RefreshToken.builder()
                .key(identifier)
                .value(oldRefreshValue)
                .expiredTime(1000L)
                .build();

        when(refreshTokenRepository.findByValue(oldRefreshValue)).thenReturn(Optional.of(existing));
        when(tokenProvider.createToken(identifier)).thenReturn("new-access");
        when(tokenProvider.createRefreshToken(identifier)).thenReturn(
                RefreshToken.builder().key(identifier).value("new-refresh").expiredTime(1000L).build()
        );

        // when
        LoginResponse resp = authService.refreshAccessToken(oldRefreshValue);

        // then
        assertEquals("new-access", resp.getAccessToken());
        assertEquals("new-refresh", resp.getRefreshToken());
        verify(refreshTokenRepository).delete(existing);
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    @DisplayName("refreshAccessToken 실패 - 잘못된 refresh")
    void refreshAccessToken_invalid() {
        // given
        String bad = "bad";
        when(refreshTokenRepository.findByValue(bad)).thenReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class, () -> authService.refreshAccessToken(bad));
        verify(refreshTokenRepository, never()).delete(any());
        verify(refreshTokenRepository, never()).save(any());
    }
}


