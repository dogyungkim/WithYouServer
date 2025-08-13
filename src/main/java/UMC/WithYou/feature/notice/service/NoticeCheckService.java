package UMC.WithYou.feature.notice.service;

import UMC.WithYou.common.apiPayload.code.status.ErrorStatus;
import UMC.WithYou.common.apiPayload.exception.handler.CommonErrorHandler;
import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.member.repository.MemberRepository;
import UMC.WithYou.feature.notice.converter.NoticeCheckConverter;
import UMC.WithYou.feature.notice.domain.Notice;
import UMC.WithYou.feature.notice.domain.NoticeCheck;
import UMC.WithYou.feature.notice.repository.NoticeCheckRepository;
import UMC.WithYou.feature.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class NoticeCheckService {

    private final NoticeCheckRepository noticeCheckRepository;
    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;

    public NoticeCheck checkBox(Long noticeId, Long memberId){
        Notice notice= noticeRepository.findById(noticeId)
                .orElseThrow(()->new CommonErrorHandler(ErrorStatus._NOTICE_NOT_FOUND));
        Member member=memberRepository.findById(memberId)
                .orElseThrow(()->new CommonErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Optional<NoticeCheck> noticeCheck=noticeCheckRepository.findByMemberAndNotice(member,notice);
        if (noticeCheck.isPresent()){
            noticeCheck.get().changeStatus();
            return noticeCheckRepository.save(noticeCheck.get());
        }
        else {
            NoticeCheck newNoticeCheck = NoticeCheckConverter.toJoinDTO(notice, member);
            return noticeCheckRepository.save(newNoticeCheck);
        }
    }
}
