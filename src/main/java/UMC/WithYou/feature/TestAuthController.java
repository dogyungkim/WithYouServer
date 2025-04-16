package UMC.WithYou.feature;

import java.util.Map;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import UMC.WithYou.feature.auth.service.JwtTokenProvider;
import UMC.WithYou.feature.member.domain.Identifier;
import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RestController
@Profile("test") // 테스트 환경에서만 활성화
@RequestMapping("/api/v1/test-auth")
@RequiredArgsConstructor
public class TestAuthController {

    private final JwtTokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    @PostMapping("/login")
    public Map<String, String> getTestToken() {
        Identifier identifier = new Identifier("test-identifier");
        
        // JWT 토큰 생성
        String token = tokenProvider.createToken(identifier.getValue());
        
        return Map.of("token", token);
    }
}