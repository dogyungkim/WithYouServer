package UMC.WithYou.service.notice;

import UMC.WithYou.domain.notice.Notice;
import UMC.WithYou.dto.notice.NoticeCheckResponseDTO;
import UMC.WithYou.dto.notice.NoticeRequestDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface NoticeCommandService {

    Notice createNotice(NoticeRequestDTO.JoinDto request);

    Notice delete(Long noticeId);

    Notice fix(NoticeRequestDTO.FixDto request);

    Notice getNotice(Long noticeId);

    List<NoticeCheckResponseDTO.ShortResponseDto> getTravelNotice(Long travelId, Long memberId);

    List<NoticeCheckResponseDTO.ShortResponseDto> getDateNotice(Long travelId , Long memberId);
}
