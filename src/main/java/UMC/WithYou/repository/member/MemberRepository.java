package UMC.WithYou.repository.member;

import UMC.WithYou.domain.member.Email;
import UMC.WithYou.domain.member.Identifier;
import UMC.WithYou.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(Email email);

    Optional<Member> findByIdentifier(Identifier identifier);
}
