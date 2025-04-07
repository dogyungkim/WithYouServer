package UMC.WithYou.feature.notice.repository;

import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.notice.domain.Notice;
import UMC.WithYou.feature.notice.domain.NoticeCheck;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoticeCheckRepository extends JpaRepository<NoticeCheck,Long> {
    Optional<NoticeCheck> findByNotice(Notice notice);
    List<NoticeCheck> findAllByIsCheckedIsTrueAndNotice(Notice notice);
    Optional<NoticeCheck> findByMemberAndNotice(Member member, Notice notice);
}
