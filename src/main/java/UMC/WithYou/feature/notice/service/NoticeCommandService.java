package UMC.WithYou.feature.notice.service;

import UMC.WithYou.feature.notice.controller.dto.NoticeCheckResponseDTO;
import UMC.WithYou.feature.notice.controller.dto.NoticeRequestDTO;
import UMC.WithYou.feature.notice.domain.Notice;

import java.util.List;

public interface NoticeCommandService {

    Notice createNotice(NoticeRequestDTO.JoinDto request);

    Notice delete(Long noticeId);

    Notice fix(NoticeRequestDTO.FixDto request);

    Notice getNotice(Long noticeId);

    List<NoticeCheckResponseDTO.ShortResponseDto> getTravelNotice(Long travelId, Long memberId);

    List<NoticeCheckResponseDTO.ShortResponseDto> getDateNotice(Long travelId , Long memberId);
}
