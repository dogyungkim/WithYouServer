package UMC.WithYou.feature.notice.converter;

import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.notice.controller.dto.NoticeCheckResponseDTO;
import UMC.WithYou.feature.notice.domain.Notice;
import UMC.WithYou.feature.notice.domain.NoticeCheck;

public class NoticeCheckConverter {

    public static NoticeCheckResponseDTO.ResultDto toResultDTO(NoticeCheck notice){
        return NoticeCheckResponseDTO.ResultDto.builder()
                .noticeId(notice.getId())
                .createdAt(notice.getCreatedAt())
                .build();
    }

    public static NoticeCheck toChangeNoticeCheck(NoticeCheck noticeCheck,Member member,Notice notice){
        boolean checkStatus=false;
        if (!noticeCheck.isChecked())
            checkStatus=true;

        return NoticeCheck.builder()
                .id(noticeCheck.getId())
                .member(member)
                .notice(notice)
                .isChecked(checkStatus)
                .build();
    }

    public static NoticeCheck toJoinDTO(Notice notices,Member member){
        return NoticeCheck.builder()
                .isChecked(true)
                .member(member)
                .notice(notices)
                .build();
    }
}
