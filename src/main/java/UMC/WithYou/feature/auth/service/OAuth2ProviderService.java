package UMC.WithYou.feature.auth.service;

import UMC.WithYou.feature.auth.controller.LoginRequest;
import UMC.WithYou.feature.auth.domain.AppleUserInfo;
import UMC.WithYou.feature.auth.domain.GoogleUserInfo;
import UMC.WithYou.feature.auth.domain.KakaoUserInfo;
import UMC.WithYou.feature.auth.domain.UserInfo;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2ProviderService {
    private final RestTemplate restTemplate;
    private final AppleTokenVerifier appleTokenVerifier;

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    // 각 공급자 API와 통신하는 메소드 구현
    public UserInfo getUserInfo(LoginRequest request) throws Exception {
        return switch (request.getProvider()) {
            case "google" -> getGoogleUserInfo(request);
            case "apple" -> getAppleUserInfo(request);
            case "kakao" -> getKakaoUserInfo(request);
            default -> throw new IllegalArgumentException("Unsupported provider: " + request.getProvider());
        };
    }

    private UserInfo getGoogleUserInfo(LoginRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(request.getAccessToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://www.googleapis.com/oauth2/v3/userinfo",
                HttpMethod.GET,
                entity,
                Map.class
        );
        Map<String, Object> attributes = response.getBody();
        return new GoogleUserInfo(attributes);
    }

    private UserInfo getAppleUserInfo(LoginRequest request) throws Exception {
        DecodedJWT jwt = appleTokenVerifier.verifyToken(request.getAccessToken(), request.getNonce());

        String userId = jwt.getSubject();
        return new AppleUserInfo(userId,request.getEmail(),request.getName());
    }

    private UserInfo getKakaoUserInfo(LoginRequest request) {
        // Kakao API를 호출하여 사용자 정보를 가져옵니다.
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(request.getAccessToken());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                entity,
                Map.class
        );

        Map<String, Object> attributes = response.getBody();

        return new KakaoUserInfo(attributes);
    }
}

