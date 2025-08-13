package UMC.WithYou.notice.domain;

import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.notice.domain.Notice;
import UMC.WithYou.feature.travel.domain.Travel;
import UMC.WithYou.feature.travel.domain.TravelStatus;
import UMC.WithYou.member.MemberFixture;
import UMC.WithYou.travel.TravelFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class NoticeTest {
    
    private Member creator;
    private Travel travel;
    private Notice notice;
    private final LocalDate today = LocalDate.now();
    private final LocalDate nextWeek = today.plusDays(7);
    
    @BeforeEach
    void setUp() {
        creator = MemberFixture.createMemberWithId(1L);
        travel = TravelFixture.createCustomTravel(creator, "제주도 여행", today, nextWeek);
    }
    
    @Nested
    @DisplayName("공지사항 생성 테스트")
    class CreateNoticeTest {
        
        @Test
        @DisplayName("여행 전 공지사항 생성 테스트")
        void createBeforeTravelNotice() {
            // given
            String content = "여행 준비물 리스트입니다.";
            TravelStatus status = TravelStatus.BEFORE;
            
            // when
            notice = Notice.builder()
                    .content(content)
                    .status(status)
                    .member(creator)
                    .travel(travel)
                    .build();
            
            // then
            assertThat(notice.getContent()).isEqualTo(content);
            assertThat(notice.getStatus()).isEqualTo(status);
            assertThat(notice.getMember()).isEqualTo(creator);
            assertThat(notice.getTravel()).isEqualTo(travel);
        }
        
        @Test
        @DisplayName("여행 중 공지사항 생성 테스트")
        void createDuringTravelNotice() {
            // given
            String content = "오늘 일정이 변경되었습니다.";
            TravelStatus status = TravelStatus.ONGOING;
            
            // when
            notice = Notice.builder()
                    .content(content)
                    .status(status)
                    .member(creator)
                    .travel(travel)
                    .build();
            
            // then
            assertThat(notice.getContent()).isEqualTo(content);
            assertThat(notice.getStatus()).isEqualTo(status);
            assertThat(notice.getMember()).isEqualTo(creator);
            assertThat(notice.getTravel()).isEqualTo(travel);
        }
        
        @Test
        @DisplayName("여행 후 공지사항 생성 테스트")
        void createAfterTravelNotice() {
            // given
            String content = "여행 사진 공유해드립니다.";
            TravelStatus status = TravelStatus.AFTER;
            
            // when
            notice = Notice.builder()
                    .content(content)
                    .status(status)
                    .member(creator)
                    .travel(travel)
                    .build();
            
            // then
            assertThat(notice.getContent()).isEqualTo(content);
            assertThat(notice.getStatus()).isEqualTo(status);
            assertThat(notice.getMember()).isEqualTo(creator);
            assertThat(notice.getTravel()).isEqualTo(travel);
        }
    }
    
    @Nested
    @DisplayName("공지사항 상태 확인 테스트")
    class NoticeStateTest {
        
        @Test
        @DisplayName("여행 전 공지사항인지 확인")
        void isBeforeTravelNotice() {
            // given
            notice = Notice.builder()
                    .content("여행 전 공지")
                    .status(TravelStatus.BEFORE)
                    .member(creator)
                    .travel(travel)
                    .build();
            
            // when & then
            assertThat(notice.getStatus()).isEqualTo(TravelStatus.BEFORE);
        }
        
        @Test
        @DisplayName("여행 중 공지사항인지 확인")
        void isDuringTravelNotice() {
            // given
            notice = Notice.builder()
                    .content("여행 중 공지")
                    .status(TravelStatus.ONGOING)
                    .member(creator)
                    .travel(travel)
                    .build();
            
            // when & then
            assertThat(notice.getStatus()).isEqualTo(TravelStatus.ONGOING);
        }
        
        @Test
        @DisplayName("여행 후 공지사항인지 확인")
        void isAfterTravelNotice() {
            // given
            notice = Notice.builder()
                    .content("여행 후 공지")
                    .status(TravelStatus.AFTER)
                    .member(creator)
                    .travel(travel)
                    .build();
            
            // when & then
            assertThat(notice.getStatus()).isEqualTo(TravelStatus.AFTER);
        }
    }
    
    @Nested
    @DisplayName("공지사항 관계 테스트")
    class NoticeRelationshipTest {
        
        @Test
        @DisplayName("공지사항 작성자 확인 테스트")
        void noticeCreator() {
            // given
            notice = Notice.builder()
                    .content("테스트 공지")
                    .status(TravelStatus.BEFORE)
                    .member(creator)
                    .travel(travel)
                    .build();
            
            // when & then
            assertThat(notice.getMember()).isEqualTo(creator);
            assertThat(notice.getMember().getId()).isEqualTo(1L);
        }
        
        @Test
        @DisplayName("공지사항 연결 여행 확인 테스트")
        void noticeTravel() {
            // given
            notice = Notice.builder()
                    .content("테스트 공지")
                    .status(TravelStatus.BEFORE)
                    .member(creator)
                    .travel(travel)
                    .build();
            
            // when & then
            assertThat(notice.getTravel()).isEqualTo(travel);
            assertThat(notice.getTravel().getTitle()).isEqualTo("제주도 여행");
        }
    }
}
