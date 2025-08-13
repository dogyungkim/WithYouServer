package UMC.WithYou.notice.repository;

import UMC.WithYou.feature.notice.domain.Notice;
import UMC.WithYou.feature.notice.repository.NoticeRepository;
import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.travel.domain.Travel;
import UMC.WithYou.feature.travel.domain.TravelStatus;
import UMC.WithYou.member.MemberFixture;
import UMC.WithYou.travel.TravelFixture;
import UMC.WithYou.notice.NoticeFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class NoticeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private NoticeRepository noticeRepository;

    private Member member;
    private Travel travel;
    private Notice notice;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 준비
        member = MemberFixture.createDefaultMember();
        entityManager.persist(member);

        travel = TravelFixture.createCustomTravel(member, "테스트 여행", LocalDate.now(), LocalDate.now().plusDays(1));
        entityManager.persist(travel);

        notice = NoticeFixture.createDefaultNotice(member, travel);
        entityManager.persist(notice);
        
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("findByTravelIdFetchJoinMember: 여행 ID로 공지사항 조회 시 Member 정보가 함께 로딩되어야 한다")
    void findByTravelIdFetchJoinMember_ShouldReturnNoticesWithMember() {
        // when
        List<Notice> notices = noticeRepository.findByTravelIdFetchJoinMember(travel.getId());

        // then
        assertThat(notices).isNotEmpty();
        assertThat(notices).hasSize(1);
        
        Notice foundNotice = notices.get(0);
        
        // Member가 지연 로딩 없이 이미 로드되었는지 확인
        assertThat(foundNotice.getMember()).isNotNull();
        assertThat(foundNotice.getMember().getName()).isEqualTo(member.getName());
        
        // Travel 정보도 정확한지 확인
        assertThat(foundNotice.getTravel().getId()).isEqualTo(travel.getId());
        
        // 내용도 정확한지 확인
        assertThat(foundNotice.getContent()).isEqualTo("테스트 공지사항입니다.");
    }
    
    @Test
    @DisplayName("findByTravelIdFetchJoinMember: 여러 공지사항이 있을 때 모두 조회되어야 한다")
    void findByTravelIdFetchJoinMember_ShouldReturnAllNoticesForTravel() {
        // given
        Member anotherMember = MemberFixture.createCustomMember(
                "another@example.com", 
                "another-identifier", 
                "다른 사용자", 
                member.getMemberType()
        );
        entityManager.persist(anotherMember);
        
        Notice anotherNotice = NoticeFixture.createCustomNotice(
                "두 번째 공지사항", 
                TravelStatus.ONGOING,
                anotherMember, 
                travel
        );
        entityManager.persist(anotherNotice);
        entityManager.flush();
        entityManager.clear();
        
        // when
        List<Notice> notices = noticeRepository.findByTravelIdFetchJoinMember(travel.getId());
        
        // then
        assertThat(notices).hasSize(2);
        
        // 공지사항 내용으로 정렬하여 검증
        notices.sort((n1, n2) -> n1.getContent().compareTo(n2.getContent()));
        
        // 두 번째 공지사항 검증
        assertThat(notices.get(0).getContent()).isEqualTo("두 번째 공지사항");
        assertThat(notices.get(0).getMember().getName()).isEqualTo("다른 사용자");
        
        // 첫 번째 공지사항 검증
        assertThat(notices.get(1).getContent()).isEqualTo("테스트 공지사항입니다.");
        assertThat(notices.get(1).getMember().getName()).isEqualTo("테스트사용자");
    }
    
    @Test
    @DisplayName("findByTravelIdFetchJoinMember: 존재하지 않는 여행 ID로 조회 시 빈 리스트를 반환해야 한다")
    void findByTravelIdFetchJoinMember_ShouldReturnEmptyListForNonExistentTravel() {
        // when
        List<Notice> notices = noticeRepository.findByTravelIdFetchJoinMember(9999L);
        
        // then
        assertThat(notices).isEmpty();
    }
}
