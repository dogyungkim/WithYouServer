package UMC.WithYou.converter;

import UMC.WithYou.domain.member.Member;
import UMC.WithYou.domain.notice.Notice;
import UMC.WithYou.domain.travel.Travel;
import UMC.WithYou.dto.notice.NoticeCheckResponseDTO;
import UMC.WithYou.dto.notice.NoticeRequestDTO;
import UMC.WithYou.dto.notice.NoticeResponseDTO;

public class NoticeConverter {

    public static NoticeResponseDTO.JoinResultDto toJoinResultDTO(Notice notice){
        return NoticeResponseDTO.JoinResultDto.builder()
                .noticeId(notice.getId())
                .createdAt(notice.getCreatedAt())
                .build();
    }

    public static NoticeResponseDTO.ResultDto toResultDTO(Notice notice){ //조회용
        return NoticeResponseDTO.ResultDto.builder()
                .content(notice.getContent())
                .build();
    }

    public static Notice toFixNotice(NoticeRequestDTO.FixDto request, Member member, Travel travel){
        return Notice.builder()
                .state(request.getState())
                .member(member)
                .travel(travel)
                .id(request.getNoticeId())
                .content(request.getContent())
                .build();
    }

    public static Notice toNotice(NoticeRequestDTO.JoinDto request, Member member, Travel travel){
        return Notice.builder()
                .content(request.getContent())
                .state(request.getState())
                .member(member)
                .travel(travel)
                .build();
    }

    public static NoticeCheckResponseDTO.ShortResponseDto toSearch(Notice notice, int checkNum ,boolean isChecked){
        return NoticeCheckResponseDTO.ShortResponseDto.builder()
                .noticeId(notice.getId())
                .content(notice.getContent())
                .url(notice.getMember().getImageUrl())
                .checkNum(checkNum)
                .isChecked(isChecked)
                .name(notice.getMember().getName())
                .build();
    }

}
