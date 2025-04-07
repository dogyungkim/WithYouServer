package UMC.WithYou.feature.notice.repository;

import java.util.List;

import UMC.WithYou.feature.notice.domain.Notice;

public interface NoticeRepositoryCustom {
    List<Notice> findByTravelLogFetchJoinMember(Long travelId);
}
