package UMC.WithYou.feature.notice.service;

import UMC.WithYou.common.apiPayload.code.status.ErrorStatus;
import UMC.WithYou.common.apiPayload.exception.handler.CommonErrorHandler;
import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.member.repository.MemberRepository;
import UMC.WithYou.feature.notice.controller.dto.NoticeCheckResponseDTO;
import UMC.WithYou.feature.notice.controller.dto.NoticeRequestDTO;
import UMC.WithYou.feature.notice.controller.dto.NoticeCheckResponseDTO.ShortResponseDto;
import UMC.WithYou.feature.notice.converter.NoticeConverter;
import UMC.WithYou.feature.notice.domain.Notice;
import UMC.WithYou.feature.notice.domain.NoticeCheck;
import UMC.WithYou.feature.notice.repository.NoticeCheckRepository;
import UMC.WithYou.feature.notice.repository.NoticeRepository;
import UMC.WithYou.feature.notice.repository.NoticeRepositoryCustom;
import UMC.WithYou.feature.travel.domain.Travel;
import UMC.WithYou.feature.travel.domain.TravelStatus;
import UMC.WithYou.feature.travel.repository.TravelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final NoticeCheckRepository noticeCheckRepository;
    private final NoticeRepositoryCustom noticeRepositoryCustom;
    private final MemberRepository memberRepository;
    private final TravelRepository travelRepository;

    private static boolean isBetween(LocalDateTime dateTime, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return dateTime.isAfter(startDateTime) && dateTime.isBefore(endDateTime);
    }

    //Will be deprecated
    public List<NoticeCheckResponseDTO.ShortResponseDto> getDateNotice(Long travelId, Long memberId) {
        Travel travel = travelRepository.findById(travelId)
                .orElseThrow(() -> new CommonErrorHandler(ErrorStatus.TRAVEL_LOG_NOT_FOUND));
        TravelStatus states = travel.getStatus();

        List<NoticeCheckResponseDTO.ShortResponseDto> results = new ArrayList<>();
        List<Notice> notices = noticeRepositoryCustom.findByTravelLogFetchJoinMember(travelId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CommonErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        for (Notice notice : notices) {
            if (notice.getStatus() != states)
                continue;

            List<NoticeCheck> noticeChecks = noticeCheckRepository.findAllByIsCheckedIsTrueAndNotice(notice);
            int checkCount = noticeChecks.size();

            // 사용자가 체크했는지 여부를 확인
            boolean isChecked = noticeCheckRepository.findByMemberAndNotice(member, notice)
                    .map(NoticeCheck::isChecked)
                    .orElse(false);

            NoticeCheckResponseDTO.ShortResponseDto check = NoticeConverter.toSearch(notice, checkCount, isChecked);
            results.add(check);
        }
        return results;
    }

    public List<NoticeCheckResponseDTO.ShortResponseDto> getTravelNotice(Long travelId , Long memberId){
        List<NoticeCheckResponseDTO.ShortResponseDto> results = new ArrayList<>();
        //QueryDSL 삭제
        //List<Notice> notices = noticeRepositoryCustom.findByTravelLogFetchJoinMember(travelId);
        List<Notice> notices = noticeRepository.findByTravelIdFetchJoinMember(travelId);

        if (notices.isEmpty()) {
            return results;
        }

        // // AS-IS
        // for (Notice notice : notices) {
        //     List<NoticeCheck> noticeChecks = noticeCheckRepository.findAllByIsCheckedIsTrueAndNotice(notice);
        //     int checkCount = noticeChecks.size();

        //     // 여기에 사용자가 체크했는지 여부를 포함
        //     boolean isChecked = false;
        //     for (NoticeCheck noticeCheck : noticeChecks) {
        //         if (noticeCheck.getMember().getId().equals(memberId)) {
        //             isChecked = true;
        //             break;
        //         }
        //     }

        //     NoticeCheckResponseDTO.ShortResponseDto dto = NoticeConverter.toSearch(notice, checkCount, isChecked);
        //     results.add(dto);
        // }
        // return results;
        
        // TO-BE
        List<Long> noticeIds = notices.stream()
            .map(Notice::getId)
            .collect(Collectors.toList());

        // 체크된 노티스 조회
        List<NoticeCheck> noticeChecks = noticeCheckRepository.findAllByNoticeIds(noticeIds);

        // 노티스 아이디별 체크 목록 맵핑
        Map<Long, List<NoticeCheck>> checksByNoticeId = noticeChecks.stream()
            .collect(Collectors.groupingBy(check -> check.getNotice().getId()));

        return notices.stream()
            .map(notice -> {
                List<NoticeCheck> checks = checksByNoticeId.getOrDefault(notice.getId(), new ArrayList<>());
                int checkCount = checks.size();
                // 사용자가 체크한 노티스 조회
                boolean isChecked = checks.stream()
                    .anyMatch(check -> check.getMember().getId().equals(memberId));
                return NoticeConverter.toSearch(notice, checkCount, isChecked);
            })
            .collect(Collectors.toList());
    }
    
    public Notice createNotice(NoticeRequestDTO.JoinDto request, Member member){
        Travel travel = travelRepository.findById(request.getLogId())
                .orElseThrow(()->new CommonErrorHandler(ErrorStatus.TRAVEL_LOG_NOT_FOUND));

        Notice newNotice = NoticeConverter.toNotice(request,member,travel);
        return noticeRepository.save(newNotice);
    }

    public void delete(Long noticeId){
        noticeRepository.deleteById(noticeId);
    }

    public Notice fix(NoticeRequestDTO.FixDto request){
        Notice notice= noticeRepository.findById(request.getNoticeId()).get();
        Notice newNotice=NoticeConverter.toFixNotice(request,notice.getMember(),notice.getTravel());

        return noticeRepository.save(newNotice);
    }
}
