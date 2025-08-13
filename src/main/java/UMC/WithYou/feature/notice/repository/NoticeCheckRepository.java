package UMC.WithYou.feature.notice.repository;

import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.notice.domain.Notice;
import UMC.WithYou.feature.notice.domain.NoticeCheck;
import io.lettuce.core.dynamic.annotation.Param;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NoticeCheckRepository extends JpaRepository<NoticeCheck,Long> {
    Optional<NoticeCheck> findByNotice(Notice notice);
    List<NoticeCheck> findAllByIsCheckedIsTrueAndNotice(Notice notice);
    Optional<NoticeCheck> findByMemberAndNotice(Member member, Notice notice);

    @Query("""
        SELECT c 
        FROM NoticeCheck c 
        JOIN FETCH c.member 
        WHERE c.notice.id IN :noticeIds
        AND c.isChecked = true
    """)
    List<NoticeCheck> findAllByNoticeIds(@Param("noticeIds") List<Long> noticeIds);
}
