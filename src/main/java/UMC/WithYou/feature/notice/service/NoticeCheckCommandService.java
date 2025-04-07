package UMC.WithYou.feature.notice.service;

import UMC.WithYou.feature.notice.domain.NoticeCheck;

public interface NoticeCheckCommandService {
    NoticeCheck checkBox(Long noticeId, Long memberId);
}
