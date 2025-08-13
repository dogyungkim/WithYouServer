package UMC.WithYou.feature.notice.controller.dto;

import UMC.WithYou.common.validation.annotation.ExistNotices;
import UMC.WithYou.feature.travel.domain.TravelStatus;
import lombok.Getter;

import java.time.LocalDateTime;

public class NoticeRequestDTO {

    @Getter
    public static class JoinDto{

        //@ExistMember
        Long memberId;

        //@ExistLog
        Long logId;

        TravelStatus status;

        String content;
    }

    @Getter
    public static class FixDto{

        @ExistNotices
        Long noticeId;

        TravelStatus status;

        String content;
    }

}
