package UMC.WithYou.member;

import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.member.domain.MemberType;

import java.lang.reflect.Field;

/**
 * Member 객체를 테스트에서 쉽게 생성하기 위한 Fixture 클래스
 */
public class MemberFixture {
    
    /**
     * 기본 Member 객체를 생성합니다.
     * @return 기본값으로 설정된 Member 객체
     */
    public static Member createDefaultMember() {
        return Member.builder()
                .email("test@example.com")
                .identifier("test-identifier")
                .name("테스트사용자")
                .memberType(MemberType.BASIC_USER)
                .build();
    }
    
    /**
     * ID가 설정된 Member 객체를 생성합니다.
     * @param id 설정할 ID 값
     * @return ID가 설정된 Member 객체
     */
    public static Member createMemberWithId(Long id) {
        Member member = createDefaultMember();
        setMemberId(member, id);
        return member;
    }
    
    /**
     * 사용자 정의 정보를 가진 Member 객체를 생성합니다.
     * @param email 이메일
     * @param identifier 식별자
     * @param name 이름
     * @param memberType 회원 유형
     * @return 사용자 정의 정보로 설정된 Member 객체
     */
    public static Member createCustomMember(String email, String identifier, String name, MemberType memberType) {
        return Member.builder()
                .email(email)
                .identifier(identifier)
                .name(name)
                .memberType(memberType)
                .build();
    }
    
    /**
     * 사용자 정의 정보와 ID를 가진 Member 객체를 생성합니다.
     * @param id 설정할 ID 값
     * @param email 이메일
     * @param identifier 식별자
     * @param name 이름
     * @param memberType 회원 유형
     * @return 사용자 정의 정보와 ID로 설정된 Member 객체
     */
    public static Member createCustomMemberWithId(Long id, String email, String identifier, String name, MemberType memberType) {
        Member member = createCustomMember(email, identifier, name, memberType);
        setMemberId(member, id);
        return member;
    }
    
    /**
     * 프로필 이미지가 설정된 Member 객체를 생성합니다.
     * @param profileImageKey 프로필 이미지 키
     * @return 프로필 이미지가 설정된 Member 객체
     */
    public static Member createMemberWithProfileImage(String profileImageKey) {
        Member member = createDefaultMember();
        member.updateImage(profileImageKey);
        return member;
    }
    
    /**
     * Member 객체의 ID를 설정합니다 (리플렉션 사용).
     * @param member ID를 설정할 Member 객체
     * @param id 설정할 ID 값
     */
    public static void setMemberId(Member member, Long id) {
        try {
            Field idField = Member.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(member, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Member ID 설정 중 에러 발생: " + e.getMessage(), e);
        }
    }
    
    /**
     * 서로 다른 ID를 가진 여러 Member 객체를 생성합니다.
     * @param count 생성할 Member 객체 수
     * @return 서로 다른 ID를 가진 Member 객체 배열
     */
    public static Member[] createMultipleMembersWithIds(int count) {
        Member[] members = new Member[count];
        for (int i = 0; i < count; i++) {
            members[i] = createMemberWithId((long) (i + 1));
        }
        return members;
    }
    
}
