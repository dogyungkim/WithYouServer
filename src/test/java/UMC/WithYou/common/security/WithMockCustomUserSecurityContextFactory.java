package UMC.WithYou.common.security;

import UMC.WithYou.feature.auth.domain.UserPrincipal;
import UMC.WithYou.feature.member.domain.Member;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

/**
 * WithMockCustomUser 애노테이션으로 설정된 사용자 정보를 기반으로 
 * SecurityContext를 생성하는 팩토리 클래스
 */
public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        // 애노테이션 값으로 Member 객체 생성
        Member member = Member.builder()
                .email(annotation.email())
                .identifier(annotation.identifier())
                .name(annotation.name())
                .memberType(annotation.memberType())
                .build();
        
        // ID 설정
        try {
            var field = Member.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(member, annotation.id());
        } catch (Exception e) {
            throw new RuntimeException("Member ID 설정 중 오류 발생", e);
        }
        
        // UserPrincipal 생성
        UserPrincipal userPrincipal = UserPrincipal.create(member);
        
        // Authentication 객체 생성
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, userPrincipal.getAuthorities());
        
        // SecurityContext 생성 및 설정
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }
} 