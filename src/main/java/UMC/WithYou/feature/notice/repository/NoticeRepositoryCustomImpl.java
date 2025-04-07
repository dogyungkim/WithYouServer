package UMC.WithYou.feature.notice.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import UMC.WithYou.feature.notice.domain.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static UMC.WithYou.feature.member.domain.QMember.member;
import static UMC.WithYou.feature.notice.domain.QNotice.notice;

@Repository
@RequiredArgsConstructor
public class NoticeRepositoryCustomImpl implements NoticeRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Notice> findByTravelLogFetchJoinMember(Long travelId){
        return jpaQueryFactory
                .selectFrom(notice)
                .join(notice.member,member).fetchJoin()
                .where(notice.travel.id.eq(travelId))
                .fetch();
    }
}
