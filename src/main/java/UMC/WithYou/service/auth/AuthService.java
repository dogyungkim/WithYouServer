package UMC.WithYou.service.auth;

import UMC.WithYou.domain.auth.RefreshToken;
import UMC.WithYou.domain.auth.UserInfo;
import UMC.WithYou.domain.member.Identifier;
import UMC.WithYou.domain.member.Member;
import UMC.WithYou.domain.member.MemberType;
import UMC.WithYou.dto.auth.LoginRequest;
import UMC.WithYou.dto.auth.LoginResponse;
import UMC.WithYou.repository.auth.RefreshTokenRepository;
import UMC.WithYou.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final OAuth2ProviderService oAuth2ProviderService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public LoginResponse authenticateOrRegisterUser(LoginRequest request) throws Exception {
        UserInfo userInfo = getUserInfo(request);
        Member member = processMember(userInfo);
        return createLoginResponse(member);
    }

    private UserInfo getUserInfo(LoginRequest request) throws Exception {
        if ("apple".equals(request.getProvider())) {
            return getAppleUserInfo(request);
        } else {
            return oAuth2ProviderService.getUserInfo(request);
        }
    }

    private UserInfo getAppleUserInfo(LoginRequest request) throws Exception {
        return oAuth2ProviderService.getUserInfo(request);
    }


    private Member processMember(UserInfo userInfo) {
        Identifier identifier = new Identifier(userInfo.getIdentifier());
        return memberRepository.findByIdentifier(identifier)
                .map(existingMember -> updateExistingMember(existingMember, userInfo))
                .orElseGet(() -> registerNewMember(userInfo));
    }

    private LoginResponse createLoginResponse(Member member) {
        String accessToken = tokenProvider.createToken(member.getIdentifier());
        RefreshToken refreshToken = tokenProvider.createRefreshToken(member.getIdentifier());
        refreshTokenRepository.save(refreshToken);
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getValue())
                .build();
    }

    private Member updateExistingMember(Member existingMember, UserInfo userInfo) {
        // 기타 필요한 정보 업데이트 로직 추가

        return memberRepository.save(existingMember);
    }

    private Member registerNewMember(UserInfo userInfo) {
        // 새 멤버 등록
        return memberRepository.save(Member.builder()
                .email(userInfo.getEmail())
                .identifier(userInfo.getIdentifier())
                .name(userInfo.getName())
                .memberType(MemberType.BASIC_USER)
                .build());
    }
}
