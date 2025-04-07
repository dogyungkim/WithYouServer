package UMC.WithYou.feature.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

import UMC.WithYou.feature.auth.domain.RefreshToken;

public interface TokenProvider {
    Authentication getAuthentication(String token);
    String resolveToken(HttpServletRequest request);
    String createToken(String payload);

    RefreshToken createRefreshToken(String payload);

    String parsePayload(String token);

    Boolean validateToken(String token);
}
