package UMC.WithYou.common.security;

import UMC.WithYou.feature.member.domain.MemberType;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 테스트에서 인증된 사용자 정보를 설정하기 위한 커스텀 애노테이션
 * <p>
 * 예시 사용법:
 * {@code @WithMockCustomUser(id = 1L, name = "테스트유저")}
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
    long id() default 1L;
    String email() default "test@example.com";
    String identifier() default "test-identifier";
    String name() default "테스트유저";
    MemberType memberType() default MemberType.BASIC_USER;
} 