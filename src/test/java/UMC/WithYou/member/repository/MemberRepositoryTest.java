package UMC.WithYou.member.repository;

import UMC.WithYou.feature.member.domain.Email;
import UMC.WithYou.feature.member.domain.Identifier;
import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.member.repository.MemberRepository;
import UMC.WithYou.member.MemberFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Nested
    @DisplayName("이메일로 회원 조회")
    class FindByEmailTest {

        @Test
        @DisplayName("존재하는 이메일로 회원을 조회하면 해당 회원이 반환된다")
        void findByEmail_ExistingEmail_ReturnsMember() {
            // given
            Member member = MemberFixture.createDefaultMember();
            entityManager.persist(member);
            entityManager.flush();
            
            Email email = new Email(member.getEmail());

            // when
            Optional<Member> foundMember = memberRepository.findByEmail(email);

            // then
            assertThat(foundMember).isPresent();
            assertThat(foundMember.get().getEmail()).isEqualTo(member.getEmail());
        }

        @Test
        @DisplayName("존재하지 않는 이메일로 조회하면 빈 Optional이 반환된다")
        void findByEmail_NonExistingEmail_ReturnsEmpty() {
            // given
            Email nonExistingEmail = new Email("non-existing@example.com");

            // when
            Optional<Member> foundMember = memberRepository.findByEmail(nonExistingEmail);

            // then
            assertThat(foundMember).isEmpty();
        }
    }

    @Nested
    @DisplayName("식별자로 회원 조회")
    class FindByIdentifierTest {

        @Test
        @DisplayName("존재하는 식별자로 회원을 조회하면 해당 회원이 반환된다")
        void findByIdentifier_ExistingIdentifier_ReturnsMember() {
            // given
            Member member = MemberFixture.createDefaultMember();
            entityManager.persist(member);
            entityManager.flush();
            
            Identifier identifier = new Identifier(member.getIdentifier());

            // when
            Optional<Member> foundMember = memberRepository.findByIdentifier(identifier);

            // then
            assertThat(foundMember).isPresent();
            assertThat(foundMember.get().getIdentifier()).isEqualTo(member.getIdentifier());
        }

        @Test
        @DisplayName("존재하지 않는 식별자로 조회하면 빈 Optional이 반환된다")
        void findByIdentifier_NonExistingIdentifier_ReturnsEmpty() {
            // given
            Identifier nonExistingIdentifier = new Identifier("non-existing-identifier");

            // when
            Optional<Member> foundMember = memberRepository.findByIdentifier(nonExistingIdentifier);

            // then
            assertThat(foundMember).isEmpty();
        }
    }
}
