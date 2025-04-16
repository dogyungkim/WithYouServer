package UMC.WithYou.travel.repository;

import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.travel.domain.Travel;
import UMC.WithYou.feature.travel.domain.TravelStatus;
import UMC.WithYou.feature.travel.repository.TravelRepository;
import UMC.WithYou.member.MemberFixture;
import UMC.WithYou.travel.TravelFixture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TravelRepositoryTest {
    
    @Autowired
    private TravelRepository travelRepository;
    
    @Autowired
    private TestEntityManager entityManager;
    
    private Member testMember;
    
    @BeforeEach
    void setUp() {
        // 테스트에 사용할 Member 객체를 생성하고 영속화
        testMember = MemberFixture.createDefaultMember();
        entityManager.persist(testMember);
        entityManager.flush();
    }
    
    @Test
    @DisplayName("초대 코드로 여행 조회 - 존재하는 초대 코드")
    void findByInvitationCode_ExistingCode() {
        // given
        String invitationCode = UUID.randomUUID().toString().substring(0, 6);
        
        // TravelFixture 사용 시 기존 Member 사용
        Travel travel = TravelFixture.createCustomTravel(
            testMember, 
            "테스트 여행", 
            LocalDate.now().plusDays(1), 
            LocalDate.now().plusDays(3)
        );
        travel.setInvitationCode(invitationCode);
        
        // Travel 객체 저장
        entityManager.persist(travel);
        entityManager.flush();
        entityManager.clear();
        
        // when
        Optional<Travel> foundTravel = travelRepository.findByInvitationCode(invitationCode);
        
        // then
        assertThat(foundTravel).isPresent();
        assertThat(foundTravel.get().getInvitationCode()).isEqualTo(invitationCode);
        assertThat(foundTravel.get().getTitle()).isEqualTo("테스트 여행");
    }
    
    @Test
    @DisplayName("초대 코드로 여행 조회 - 존재하지 않는 초대 코드")
    void findByInvitationCode_NonExistingCode() {
        // given
        String nonExistingCode = "NONEXIST";
        
        // when
        Optional<Travel> foundTravel = travelRepository.findByInvitationCode(nonExistingCode);
        
        // then
        assertThat(foundTravel).isEmpty();
    }
    
    @Test
    @DisplayName("여러 개의 여행 중 특정 초대 코드를 가진 여행만 조회")
    void findByInvitationCode_MultipleTravel() {
        // given
        String targetInvitationCode = "TARGET";
        
        // 여러 개의 Travel 객체 생성 및 저장
        Travel travel1 = TravelFixture.createCustomTravel(
            testMember, 
            "여행1", 
            LocalDate.now().plusDays(1), 
            LocalDate.now().plusDays(3)
        );
        travel1.setInvitationCode("CODE1");
        
        Travel travel2 = TravelFixture.createCustomTravel(
            testMember, 
            "여행2", 
            LocalDate.now().plusDays(1), 
            LocalDate.now().plusDays(3)
        );
        travel2.setInvitationCode(targetInvitationCode);
        
        Travel travel3 = TravelFixture.createCustomTravel(
            testMember, 
            "여행3", 
            LocalDate.now().plusDays(1), 
            LocalDate.now().plusDays(3)
        );
        travel3.setInvitationCode("CODE3");
        
        entityManager.persist(travel1);
        entityManager.persist(travel2);
        entityManager.persist(travel3);
        entityManager.flush();
        entityManager.clear();
        
        // when
        Optional<Travel> foundTravel = travelRepository.findByInvitationCode(targetInvitationCode);
        
        // then
        assertThat(foundTravel).isPresent();
        assertThat(foundTravel.get().getInvitationCode()).isEqualTo(targetInvitationCode);
        assertThat(foundTravel.get().getTitle()).isEqualTo("여행2");
    }
    
    @Test
    @DisplayName("초대 코드가 null인 경우에 대한 테스트")
    void findByInvitationCode_NullCode() {
        // given
        Travel travel1 = TravelFixture.createCustomTravel(
            testMember, 
            "초대코드 있는 여행", 
            LocalDate.now().plusDays(1), 
            LocalDate.now().plusDays(3)
        );
        travel1.setInvitationCode("CODE1");
        
        Travel travel2 = TravelFixture.createCustomTravel(
            testMember, 
            "초대코드 없는 여행", 
            LocalDate.now().plusDays(1), 
            LocalDate.now().plusDays(3)
        );
        // 초대 코드를 설정하지 않음 (null)
        
        entityManager.persist(travel1);
        entityManager.persist(travel2);
        entityManager.flush();
        entityManager.clear();
        
        // when
        Optional<Travel> foundTravel = travelRepository.findByInvitationCode(null);
        
        // then
        assertThat(foundTravel.get().getInvitationCode()).isNull();
        assertThat(foundTravel.get().getTitle()).isEqualTo("초대코드 없는 여행");
    }
    
    @Test
    @DisplayName("다양한 상태의 여행을 초대 코드로 조회")
    void findByInvitationCode_DifferentStatus() {
        // given
        LocalDate today = LocalDate.now();
        
        // UPCOMING 상태의 여행
        Travel upcomingTravel = TravelFixture.createCustomTravel(
            testMember,
            "예정된 여행",
            today.plusDays(5),
            today.plusDays(10)
        );
        upcomingTravel.setTravelStatus(today);
        upcomingTravel.setInvitationCode("UPCOMING");
        
        // ONGOING 상태의 여행
        Travel ongoingTravel = TravelFixture.createCustomTravel(
            testMember,
            "진행 중인 여행",
            today.minusDays(2),
            today.plusDays(3)
        );
        ongoingTravel.setTravelStatus(today);
        ongoingTravel.setInvitationCode("ONGOING");
        
        // BYGONE 상태의 여행
        Travel bygoneTravel = TravelFixture.createCustomTravel(
            testMember,
            "지난 여행",
            today.minusDays(10),
            today.minusDays(5)
        );
        bygoneTravel.setTravelStatus(today);
        bygoneTravel.setInvitationCode("BYGONE");
        
        entityManager.persist(upcomingTravel);
        entityManager.persist(ongoingTravel);
        entityManager.persist(bygoneTravel);
        entityManager.flush();
        entityManager.clear();
        
        // when
        Optional<Travel> foundUpcoming = travelRepository.findByInvitationCode("UPCOMING");
        Optional<Travel> foundOngoing = travelRepository.findByInvitationCode("ONGOING");
        Optional<Travel> foundBygone = travelRepository.findByInvitationCode("BYGONE");
        
        // then
        assertThat(foundUpcoming).isPresent();
        assertThat(foundUpcoming.get().getStatus()).isEqualTo(TravelStatus.UPCOMING);
        
        assertThat(foundOngoing).isPresent();
        assertThat(foundOngoing.get().getStatus()).isEqualTo(TravelStatus.ONGOING);
        
        assertThat(foundBygone).isPresent();
        assertThat(foundBygone.get().getStatus()).isEqualTo(TravelStatus.BYGONE);
    }
}
