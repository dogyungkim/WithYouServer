package UMC.WithYou.travel.domain;

import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.travel.domain.Travel;
import UMC.WithYou.feature.travel.domain.TravelStatus;
import UMC.WithYou.feature.travel.domain.Traveler;
import UMC.WithYou.member.MemberFixture;
import UMC.WithYou.travel.TravelFixture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class TravelTest {

    private Member owner;
    private Travel travel;
    private final LocalDate today = LocalDate.now();
    private final LocalDate yesterday = today.minusDays(1);
    private final LocalDate tomorrow = today.plusDays(1);
    private final LocalDate nextWeek = today.plusDays(7);
    private final LocalDate lastWeek = today.minusDays(7);

    @BeforeEach
    void setUp() {
        // MemberFixture를 사용하여 owner 생성
        owner = MemberFixture.createMemberWithId(1L);
        // TravelFixture 대신 직접 Travel 객체 생성 (날짜 설정을 위해)
        travel = TravelFixture.createCustomTravel(owner, "제주도 여행", today, nextWeek);
    }

    @Test
    @DisplayName("여행 생성 시 기본 필드가 올바르게 설정되는지 확인")
    void createTravel() {
        // when & then
        assertThat(travel.getMember()).isEqualTo(owner);
        assertThat(travel.getTitle()).isEqualTo("제주도 여행");
        assertThat(travel.getStartDate()).isEqualTo(today);
        assertThat(travel.getEndDate()).isEqualTo(nextWeek);
        assertThat(travel.getTravelers()).isEmpty();
    }

    @Test
    @DisplayName("여행 정보 수정 기능 테스트")
    void editTravel() {
        // when
        travel.edit("부산 여행", tomorrow, nextWeek.plusDays(2));

        // then
        assertThat(travel.getTitle()).isEqualTo("부산 여행");
        assertThat(travel.getStartDate()).isEqualTo(tomorrow);
        assertThat(travel.getEndDate()).isEqualTo(nextWeek.plusDays(2));
    }

    @Nested
    @DisplayName("여행 상태 설정 테스트")
    class TravelStatusTest {

        @Test
        @DisplayName("종료일이 현재보다 이전이면 BYGONE 상태로 설정")
        void setStatusToBygone() {
            // given
            Travel pastTravel = TravelFixture.createCustomTravel(owner, "지난 여행", lastWeek, yesterday);

            // when
            pastTravel.setTravelStatus(today);

            // then
            assertThat(pastTravel.getStatus()).isEqualTo(TravelStatus.BYGONE);
        }

        @Test
        @DisplayName("현재 날짜가 시작일과 종료일 사이면 ONGOING 상태로 설정")
        void setStatusToOngoing() {
            // given
            Travel ongoingTravel = TravelFixture.createCustomTravel(owner, "진행 중인 여행", yesterday, tomorrow);

            // when
            ongoingTravel.setTravelStatus(today);

            // then
            assertThat(ongoingTravel.getStatus()).isEqualTo(TravelStatus.ONGOING);
        }

        @Test
        @DisplayName("시작일이 현재보다 이후면 UPCOMING 상태로 설정")
        void setStatusToUpcoming() {
            // given
            Travel upcomingTravel = TravelFixture.createCustomTravel(owner, "예정된 여행", tomorrow, nextWeek);

            // when
            upcomingTravel.setTravelStatus(today);

            // then
            assertThat(upcomingTravel.getStatus()).isEqualTo(TravelStatus.UPCOMING);
        }

        @Test
        @DisplayName("시작일이 현재와 같으면 ONGOING 상태로 설정")
        void setStatusToOngoingWhenStartDateIsToday() {
            // given
            Travel todayStartTravel = TravelFixture.createCustomTravel(owner, "오늘 시작하는 여행", today, nextWeek);

            // when
            todayStartTravel.setTravelStatus(today);

            // then
            assertThat(todayStartTravel.getStatus()).isEqualTo(TravelStatus.ONGOING);
        }
    }

    @Nested
    @DisplayName("여행자 관련 기능 테스트")
    class TravelerTest {

        private Member travelerMember;
        private Traveler traveler;

        @BeforeEach
        void setUp() {
            travelerMember = MemberFixture.createMemberWithId(2L);
            traveler = TravelFixture.createTraveler(travel, travelerMember);
        }

        @Test
        @DisplayName("여행자 추가 테스트")
        void addTraveler() {
            // when
            travel.addTravelMember(traveler);

            // then
            assertThat(travel.getTravelers()).contains(traveler);
            assertThat(travel.getTravelers()).hasSize(1);
        }

        @Test
        @DisplayName("여행자 목록 조회 테스트")
        void getTravelMembers() {
            // given
            travel.addTravelMember(traveler);

            // when
            List<Member> travelMembers = travel.getTravelMembers();

            // then
            assertThat(travelMembers).contains(travelerMember);
            assertThat(travelMembers).hasSize(1);
        }

        @Test
        @DisplayName("특정 멤버가 여행자인지 확인")
        void isTraveler() {
            // given
            travel.addTravelMember(traveler);

            // when & then
            assertThat(travel.isTraveler(travelerMember)).isTrue();
            assertThat(travel.isTraveler(owner)).isFalse();
        }

        @Test
        @DisplayName("여행자 탈퇴 테스트")
        void leaveTraveler() {
            // given
            travel.addTravelMember(traveler);
            assertThat(travel.getTravelers()).hasSize(1);

            // when
            travel.leave(travelerMember);

            // then
            assertThat(travel.getTravelers()).isEmpty();
        }
    }

    @Test
    @DisplayName("여행 소유권 검증 테스트")
    void validateOwnership() {
        // given
        Member otherMember = MemberFixture.createMemberWithId(3L);

        // when & then
        assertThat(travel.validateOwnership(owner)).isTrue();
        assertThat(travel.validateOwnership(otherMember)).isFalse();
    }

    @Nested
    @DisplayName("초대코드 관련 테스트")
    class InvitationCodeTest {

        @Test
        @DisplayName("초대코드가 없으면 false 반환")
        void hasNoInvitationCode() {
            // when & then
            assertThat(travel.hasInvitationCode()).isFalse();
        }

        @Test
        @DisplayName("초대코드 설정 및 확인 테스트")
        void setAndCheckInvitationCode() {
            // when
            travel.setInvitationCode("INVITE123");

            // then
            assertThat(travel.hasInvitationCode()).isTrue();
            assertThat(travel.getInvitationCode()).isEqualTo("INVITE123");
        }
    }
}
