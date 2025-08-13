package UMC.WithYou.notice.service;

import UMC.WithYou.common.apiPayload.code.status.ErrorStatus;
import UMC.WithYou.common.apiPayload.exception.handler.CommonErrorHandler;
import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.member.repository.MemberRepository;
import UMC.WithYou.feature.notice.controller.dto.NoticeCheckResponseDTO;
import UMC.WithYou.feature.notice.controller.dto.NoticeRequestDTO;
import UMC.WithYou.feature.notice.converter.NoticeConverter;
import UMC.WithYou.feature.notice.domain.Notice;
import UMC.WithYou.feature.notice.domain.NoticeCheck;
import UMC.WithYou.feature.notice.repository.NoticeCheckRepository;
import UMC.WithYou.feature.notice.repository.NoticeRepository;
import UMC.WithYou.feature.notice.repository.NoticeRepositoryCustom;
import UMC.WithYou.feature.notice.service.NoticeService;
import UMC.WithYou.feature.travel.domain.Travel;
import UMC.WithYou.feature.travel.domain.TravelStatus;
import UMC.WithYou.feature.travel.repository.TravelRepository;
import UMC.WithYou.member.MemberFixture;
import UMC.WithYou.notice.NoticeFixture;
import UMC.WithYou.travel.TravelFixture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NoticeServiceTest {
    
    @Mock
    private NoticeRepository noticeRepository;
    
    @Mock
    private NoticeCheckRepository noticeCheckRepository;
    
    @Mock
    private NoticeRepositoryCustom noticeRepositoryCustom;
    
    @Mock
    private MemberRepository memberRepository;
    
    @Mock
    private TravelRepository travelRepository;
    
    @Mock
    private NoticeConverter noticeConverter;
    
    @InjectMocks
    private NoticeService noticeService;
    
    private Member testMember;
    private Travel testTravel;
    private Notice testNotice;
    private NoticeCheck testNoticeCheck;
    private final LocalDate today = LocalDate.now();
    
    @BeforeEach
    void setUp() {
        testMember = MemberFixture.createMemberWithId(1L);
        testTravel = TravelFixture.createCustomTravelWithId(1L, testMember, "테스트 여행", today, today.plusDays(7));
        testTravel.setTravelStatus(today);
        testNotice = NoticeFixture.createNoticeWithId(1L, testMember, testTravel);
        testNoticeCheck = NoticeFixture.createNoticeCheckWithId(1L, testMember, testNotice);
    }
    
    @Nested
    @DisplayName("공지사항 생성 테스트")
    class CreateNoticeTest {
        
        @Test
        @DisplayName("공지사항 생성 성공")
        void createNotice_Success() {
            // given
            NoticeRequestDTO.JoinDto joinDto = mock(NoticeRequestDTO.JoinDto.class);
            when(joinDto.getLogId()).thenReturn(1L);
            
            when(travelRepository.findById(1L)).thenReturn(Optional.of(testTravel));
            when(noticeRepository.save(any(Notice.class))).thenReturn(testNotice);
            
            // when
            Notice createdNotice = noticeService.createNotice(joinDto, testMember);
            
            // then
            assertThat(createdNotice).isEqualTo(testNotice);
            verify(noticeRepository).save(any(Notice.class));
        }
        
        @Test
        @DisplayName("공지사항 생성 실패 - 여행이 존재하지 않는 경우")
        void createNotice_Fail_TravelNotFound() {
            // given
            NoticeRequestDTO.JoinDto joinDto = mock(NoticeRequestDTO.JoinDto.class);
            when(joinDto.getLogId()).thenReturn(999L);
            
            when(travelRepository.findById(anyLong())).thenReturn(Optional.empty());
            
            // when & then
            assertThatThrownBy(() -> noticeService.createNotice(joinDto, testMember))
                .isInstanceOf(CommonErrorHandler.class)
                .extracting(ex -> ((CommonErrorHandler) ex).getCode())
                .isEqualTo(ErrorStatus.TRAVEL_LOG_NOT_FOUND);
                
            verify(noticeRepository, never()).save(any(Notice.class));
        }
    }
    
    @Nested
    @DisplayName("공지사항 삭제 테스트")
    class DeleteNoticeTest {
        
        @Test
        @DisplayName("공지사항 삭제 성공")
        void deleteNotice_Success() {
            // given
            doNothing().when(noticeRepository).deleteById(1L);
            
            // when
            noticeService.delete(1L);
            
            // then
            verify(noticeRepository).deleteById(1L);
        }
    }
    
    @Nested
    @DisplayName("공지사항 수정 테스트")
    class UpdateNoticeTest {
        
        @Test
        @DisplayName("공지사항 수정 성공")
        void updateNotice_Success() {
            // given
            NoticeRequestDTO.FixDto fixDto = mock(NoticeRequestDTO.FixDto.class);
            when(fixDto.getNoticeId()).thenReturn(1L);
            
            when(noticeRepository.findById(1L)).thenReturn(Optional.of(testNotice));
            when(noticeRepository.save(any(Notice.class))).thenReturn(testNotice);
            
            // when
            Notice updatedNotice = noticeService.fix(fixDto);
            
            // then
            assertThat(updatedNotice).isEqualTo(testNotice);
            verify(noticeRepository).save(any(Notice.class));
        }
    }
    
    @Nested
    @DisplayName("공지사항 조회 테스트")
    class GetNoticeTest {
        
        @Test
        @DisplayName("여행별 공지사항 목록 조회 성공")
        void getTravelNotice_Success() {
            // given
            // 여러 개의 Notice 생성
            Notice testNotice1 = Notice.builder()
                    .id(1L)
                    .content("첫번째 공지사항")
                    .status(testTravel.getStatus())
                    .member(testMember)
                    .travel(testTravel)
                    .build();
            
            Notice testNotice2 = Notice.builder()
                    .id(2L)
                    .content("두번째 공지사항")
                    .status(testTravel.getStatus())
                    .member(testMember)
                    .travel(testTravel)
                    .build();
            
            // 여러 개의 NoticeCheck 생성
            NoticeCheck testNoticeCheck1 = NoticeCheck.builder()
                    .id(1L)
                    .notice(testNotice1)
                    .member(testMember)
                    .isChecked(true)
                    .build();
            
            Member secondMember = MemberFixture.createMemberWithId(2L);
            
            NoticeCheck testNoticeCheck2 = NoticeCheck.builder()
                    .id(2L)
                    .notice(testNotice1)
                    .member(secondMember)
                    .isChecked(true)
                    .build();
            
            NoticeCheck testNoticeCheck3 = NoticeCheck.builder()
                    .id(3L)
                    .notice(testNotice2)
                    .member(testMember)
                    .isChecked(true)
                    .build();
            
            NoticeCheck testNoticeCheck4 = NoticeCheck.builder()
                    .id(4L)
                    .notice(testNotice2)
                    .member(secondMember)
                    .isChecked(false)
                    .build();
            
            List<Notice> notices = Arrays.asList(testNotice1, testNotice2);
            List<NoticeCheck> noticeChecks = Arrays.asList(
                    testNoticeCheck1, testNoticeCheck2, testNoticeCheck3, testNoticeCheck4);
            
            // Notice ID 목록 생성
            List<Long> noticeIds = Arrays.asList(1L, 2L);
            
            // Mock 설정
            when(noticeRepository.findByTravelIdFetchJoinMember(1L)).thenReturn(notices);
            when(noticeCheckRepository.findAllByNoticeIds(noticeIds)).thenReturn(noticeChecks.stream().filter(NoticeCheck::isChecked).collect(Collectors.toList()));
            
            // when
            List<NoticeCheckResponseDTO.ShortResponseDto> result = noticeService.getTravelNotice(1L, 1L);
            for (NoticeCheckResponseDTO.ShortResponseDto dto : result) {
                System.out.println(dto.getContent());
                System.out.println(dto.getNoticeId());
                System.out.println(dto.getCheckNum());
                System.out.println(dto.isChecked());
            }
            // then
            assertThat(result).hasSize(2);
            
            // 첫 번째 공지사항 검증
            assertThat(result.get(0).getNoticeId()).isEqualTo(1L);
            assertThat(result.get(0).getContent()).isEqualTo("첫번째 공지사항");
            assertThat(result.get(0).getCheckNum()).isEqualTo(2); // 2명이 확인했으므로
            assertThat(result.get(0).isChecked()).isTrue(); // 테스트 사용자(1L)가 확인함
            
            // 두 번째 공지사항 검증
            assertThat(result.get(1).getNoticeId()).isEqualTo(2L);
            assertThat(result.get(1).getContent()).isEqualTo("두번째 공지사항");
            assertThat(result.get(1).getCheckNum()).isEqualTo(1); 
            assertThat(result.get(1).isChecked()).isTrue(); // 테스트 사용자(1L)가 확인함
            
            // 메서드 호출 검증
            verify(noticeRepository).findByTravelIdFetchJoinMember(1L);
            verify(noticeCheckRepository).findAllByNoticeIds(noticeIds);
        }
        
        @Test
        @DisplayName("날짜별 공지사항 목록 조회 성공")
        void getDateNotice_Success() {
            // given
            List<Notice> notices = Collections.singletonList(testNotice);
            
            when(travelRepository.findById(1L)).thenReturn(Optional.of(testTravel));
            when(memberRepository.findById(1L)).thenReturn(Optional.of(testMember));
            when(noticeRepositoryCustom.findByTravelLogFetchJoinMember(1L)).thenReturn(notices);
            
            // when
            List<NoticeCheckResponseDTO.ShortResponseDto> result = noticeService.getDateNotice(1L, 1L);
            
            // then
            verify(travelRepository).findById(1L);
            verify(noticeRepositoryCustom).findByTravelLogFetchJoinMember(1L);
        }
        
        @Test
        @DisplayName("TravelId로 공지사항 목록 조회 성공")
        void getNotices_Success() {
            // given
            List<Notice> notices = Collections.singletonList(testNotice);
            
            when(noticeRepository.findByTravelIdFetchJoinMember(1L)).thenReturn(notices);
            
            // when
            List<NoticeCheckResponseDTO.ShortResponseDto> result = noticeService.getTravelNotice(1L, 1L);
            
            // then
            assertThat(result).isNotEmpty();
        }
        
        @Test
        @DisplayName("날짜별 공지사항 목록 조회 실패 - 여행이 존재하지 않는 경우")
        void getDateNotice_Fail_TravelNotFound() {
            // given
            when(travelRepository.findById(anyLong())).thenReturn(Optional.empty());
            
            // when & then
            assertThatThrownBy(() -> noticeService.getDateNotice(1L, 1L))
                .isInstanceOf(CommonErrorHandler.class)
                .extracting(ex -> ((CommonErrorHandler) ex).getCode())
                .isEqualTo(ErrorStatus.TRAVEL_LOG_NOT_FOUND);
        }
        
        @Test
        @DisplayName("날짜별 공지사항 목록 조회 실패 - 회원이 존재하지 않는 경우")
        void getDateNotice_Fail_MemberNotFound() {
            // given
            when(travelRepository.findById(anyLong())).thenReturn(Optional.of(testTravel));
            when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());
            
            // when & then
            assertThatThrownBy(() -> noticeService.getDateNotice(1L, 1L))
                .isInstanceOf(CommonErrorHandler.class)
                .extracting(ex -> ((CommonErrorHandler) ex).getCode())
                .isEqualTo(ErrorStatus.MEMBER_NOT_FOUND);
        }
    }
}
