package UMC.WithYou.notice.service;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.member.domain.MemberType;
import UMC.WithYou.feature.member.repository.MemberRepository;
import UMC.WithYou.feature.notice.controller.dto.NoticeCheckResponseDTO;
import UMC.WithYou.feature.notice.domain.Notice;
import UMC.WithYou.feature.notice.domain.NoticeCheck;
import UMC.WithYou.feature.notice.repository.NoticeCheckRepository;
import UMC.WithYou.feature.notice.repository.NoticeRepository;
import UMC.WithYou.feature.notice.service.NoticeService;
import UMC.WithYou.feature.travel.domain.Travel;
import UMC.WithYou.feature.travel.domain.TravelStatus;
import UMC.WithYou.feature.travel.repository.TravelRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class NoticeIntegrationTest {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private NoticeCheckRepository noticeCheckRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TravelRepository travelRepository;
    
    private Member member1;
    private Member member2;
    private Travel travel;
    private Notice notice1;
    private Notice notice2;
    
    @BeforeEach
    void setUp() {
        // 회원 생성 및 저장
        member1 = Member.builder()
                .email("test1@example.com")
                .identifier("testuser1")
                .name("테스트 사용자1")
                .memberType(MemberType.BASIC_USER)
                .build();
        member1 = memberRepository.save(member1);
        
        member2 = Member.builder()
                .email("test2@example.com")
                .identifier("testuser2")
                .name("테스트 사용자2")
                .memberType(MemberType.BASIC_USER)
                .build();
        member2 = memberRepository.save(member2);
        
        // 여행 생성 및 저장
        travel = new Travel(member1, "테스트 여행", LocalDate.now(), LocalDate.now().plusDays(3));
        travel.setTravelStatus(LocalDate.now()); // 상태 설정
        travel = travelRepository.save(travel);
        
        // 공지사항 생성 및 저장
        notice1 = Notice.builder()
                .content("첫번째 테스트 공지사항")
                .status(TravelStatus.BEFORE)
                .member(member1)
                .travel(travel)
                .build();
        notice1 = noticeRepository.save(notice1);
        
        notice2 = Notice.builder()
                .content("두번째 테스트 공지사항")
                .status(TravelStatus.ONGOING)
                .member(member1)
                .travel(travel)
                .build();
        notice2 = noticeRepository.save(notice2);
        
        // NoticeCheck 생성 및 저장 (member1이 notice1 확인)
        NoticeCheck noticeCheck1 = NoticeCheck.builder()
                .notice(notice1)
                .member(member1)
                .isChecked(true)
                .build();
        noticeCheckRepository.save(noticeCheck1);
        
        // NoticeCheck 생성 및 저장 (member2가 notice1 확인)
        NoticeCheck noticeCheck2 = NoticeCheck.builder()
                .notice(notice1)
                .member(member2)
                .isChecked(true)
                .build();
        noticeCheckRepository.save(noticeCheck2);
        
        // NoticeCheck 생성 및 저장 (member1이 notice2 확인하지 않음)
        NoticeCheck noticeCheck3 = NoticeCheck.builder()
                .notice(notice2)
                .member(member1)
                .isChecked(false)
                .build();
        noticeCheckRepository.save(noticeCheck3);
        
        // NoticeCheck 생성 및 저장 (member2가 notice2 확인)
        NoticeCheck noticeCheck4 = NoticeCheck.builder()
                .notice(notice2)
                .member(member2)
                .isChecked(true)
                .build();
        noticeCheckRepository.save(noticeCheck4);
    }

    @Test
    @DisplayName("여행별 공지사항 목록 조회 성공")
    void getTravelNotice_Success() {
        // when
        List<NoticeCheckResponseDTO.ShortResponseDto> result = noticeService.getTravelNotice(travel.getId(), member1.getId());
        for (NoticeCheckResponseDTO.ShortResponseDto dto : result) {
            System.out.println(dto.getContent());
            System.out.println(dto.getCheckNum());
            System.out.println(dto.isChecked());
        }
        
        // then
        assertThat(result).hasSize(2);
        
        // notice1은 2명이 확인
        assertThat(result.get(0).getCheckNum()).isEqualTo(2);
        // member1이 notice1을 확인함
        assertThat(result.get(0).isChecked()).isTrue();
        
        // notice2는 1명만 확인
        assertThat(result.get(1).getCheckNum()).isEqualTo(1);
        // member1은 notice2를 확인하지 않음
        assertThat(result.get(1).isChecked()).isFalse();
    }
}
