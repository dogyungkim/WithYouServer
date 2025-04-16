package UMC.WithYou.member.domain;

import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.member.domain.MemberType;
import UMC.WithYou.feature.travel.domain.Traveler;
import UMC.WithYou.member.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class MemberTest {

    @Nested
    @DisplayName("회원 식별 테스트")
    class IdentificationTest {
        
        @Test
        @DisplayName("동일한 ID를 가진 회원인지 확인한다")
        void isSameId() {
            // given
            Member member = MemberFixture.createMemberWithId(1L);
            
            // when & then
            assertThat(member.isSameId(1L)).isTrue();
            assertThat(member.isSameId(2L)).isFalse();
        }

        @Test
        @DisplayName("같은 ID와 identifier를 가진 회원은 동일하다고 판단한다")
        void equals_SameIdAndIdentifier_ReturnsTrue() {
            // given
            Member member1 = MemberFixture.createCustomMemberWithId(1L, 
                "test1@example.com", "same-identifier", "사용자1", MemberType.BASIC_USER);
            Member member2 = MemberFixture.createCustomMemberWithId(1L, 
                "test2@example.com", "same-identifier", "사용자2", MemberType.BASIC_USER);

            // when & then
            assertThat(member1).isEqualTo(member2);
            assertThat(member1.hashCode()).isEqualTo(member2.hashCode());
        }

        @Test
        @DisplayName("ID나 identifier가 다른 회원은 다른 회원으로 판단한다")
        void equals_DifferentIdOrIdentifier_ReturnsFalse() {
            // given
            Member member1 = MemberFixture.createCustomMemberWithId(1L, 
                "test1@example.com", "identifier1", "사용자1", MemberType.BASIC_USER);
            Member member2 = MemberFixture.createCustomMemberWithId(2L, 
                "test2@example.com", "identifier2", "사용자2", MemberType.BASIC_USER);

            // when & then
            assertThat(member1).isNotEqualTo(member2);
            assertThat(member1.hashCode()).isNotEqualTo(member2.hashCode());
        }
    }

    @Nested
    @DisplayName("회원 정보 업데이트 테스트")
    class UpdateTest {
        
        @Test
        @DisplayName("회원 이름을 업데이트한다")
        void updateName() {
            // given
            Member member = MemberFixture.createDefaultMember();
            String newName = "새로운이름";

            // when
            member.updateName(newName);

            // then
            assertThat(member.getName()).isEqualTo(newName);
        }

        @Test
        @DisplayName("프로필 이미지를 업데이트한다")
        void updateImage() {
            // given
            Member member = MemberFixture.createDefaultMember();
            String newImageKey = "new-image-key";

            // when
            member.updateImage(newImageKey);

            // then
            assertThat(member.getProfileImageKey()).isEqualTo(newImageKey);
        }
    }

    @Nested
    @DisplayName("여행자 관리 테스트")
    class TravelerManagementTest {
        
        @Test
        @DisplayName("여행자를 추가한다")
        void addTraveler() {
            // given
            Member member = MemberFixture.createDefaultMember();
            Traveler mockTraveler = mock(Traveler.class);

            // when
            member.addTraveler(mockTraveler);

            // then
            assertThat(member.getTravelers())
                .hasSize(1)
                .contains(mockTraveler);
        }

        @Test
        @DisplayName("여러 여행자를 추가할 수 있다")
        void addMultipleTravelers() {
            // given
            Member member = MemberFixture.createDefaultMember();
            Traveler mockTraveler1 = mock(Traveler.class);
            Traveler mockTraveler2 = mock(Traveler.class);

            // when
            member.addTraveler(mockTraveler1);
            member.addTraveler(mockTraveler2);

            // then
            assertThat(member.getTravelers())
                .hasSize(2)
                .containsExactly(mockTraveler1, mockTraveler2);
        }
    }
}
