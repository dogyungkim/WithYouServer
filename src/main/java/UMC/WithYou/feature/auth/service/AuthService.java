package UMC.WithYou.feature.auth.service;

import UMC.WithYou.feature.auth.controller.LoginRequest;
import UMC.WithYou.feature.auth.controller.LoginResponse;
import UMC.WithYou.feature.auth.domain.RefreshToken;
import UMC.WithYou.feature.auth.domain.UserInfo;
import UMC.WithYou.feature.auth.repository.RefreshTokenRepository;
import UMC.WithYou.feature.member.domain.Identifier;
import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.member.domain.MemberType;
import UMC.WithYou.feature.member.repository.MemberRepository;
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
        return oAuth2ProviderService.getUserInfo(request);
    }


    private Member processMember(UserInfo userInfo) {
        Identifier identifier = new Identifier(userInfo.getIdentifier());
        return memberRepository.findByIdentifier(identifier)
                .map(existingMember -> updateExistingMember(existingMember, userInfo))
                .orElseGet(() -> registerNewMember(userInfo));
    }

    @Transactional
    private LoginResponse createLoginResponse(Member member) {
        String accessToken = tokenProvider.createToken(member.getIdentifier());
        RefreshToken refreshToken = tokenProvider.createRefreshToken(member.getIdentifier());
        refreshTokenRepository.save(refreshToken);
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getValue())
                .build();
    }

    @Transactional 
    private Member updateExistingMember(Member existingMember, UserInfo userInfo) {
        // 기타 필요한 정보 업데이트 로직 추가

        return memberRepository.save(existingMember);
    }

    @Transactional
    private Member registerNewMember(UserInfo userInfo) {
        // 새 멤버 등록
        return memberRepository.save(Member.builder()
                .email(userInfo.getEmail())
                .identifier(userInfo.getIdentifier())
                .name(userInfo.getName())
                .memberType(MemberType.BASIC_USER)
                .build());
    }

    @Transactional
    public LoginResponse generateTestToken(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find member with id: " + memberId));
        return createLoginResponse(member);
    }

    @Transactional(readOnly = true)
    public boolean isValidAccessToken(String accessToken) {
        return tokenProvider.validateToken(accessToken);
    }

    @Transactional
    public LoginResponse refreshAccessToken(String refreshTokenValue) {
        RefreshToken refreshToken = refreshTokenRepository.findByValue(refreshTokenValue)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

        String identifier = refreshToken.getKey();

        String newAccessToken = tokenProvider.createToken(identifier);
        RefreshToken newRefreshToken = tokenProvider.createRefreshToken(identifier);

        refreshTokenRepository.delete(refreshToken);
        refreshTokenRepository.save(newRefreshToken);

        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken.getValue())
                .build();
    }
}
