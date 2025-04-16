package UMC.WithYou.travel.service;

import UMC.WithYou.common.apiPayload.code.BaseErrorCode;
import UMC.WithYou.common.apiPayload.code.status.ErrorStatus;
import UMC.WithYou.common.apiPayload.exception.handler.CommonErrorHandler;
import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.member.repository.MemberRepository;
import UMC.WithYou.feature.travel.domain.Travel;
import UMC.WithYou.feature.travel.domain.Traveler;
import UMC.WithYou.feature.travel.repository.TravelRepository;
import UMC.WithYou.feature.travel.service.TravelService;
import UMC.WithYou.infra.s3.S3FileType;
import UMC.WithYou.infra.s3.S3PreSignService;
import UMC.WithYou.infra.s3.S3Service;
import UMC.WithYou.member.MemberFixture;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TravelServiceTest {

    @Mock
    private TravelRepository travelRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private S3Service s3Service;

    @Mock
    private S3PreSignService s3Interface;

    @InjectMocks
    private TravelService travelService;

    private Member testMember;
    private Travel testTravel;
    private final LocalDate today = LocalDate.now();

    @BeforeEach
    void setUp() {
        testMember = MemberFixture.createMemberWithId(1L);
        testTravel = TravelFixture.createCustomTravelWithId(1L, testMember, "테스트 여행", today, today.plusDays(7));
    }

    @Nested
    @DisplayName("여행 생성 테스트")
    class CreateTravelTest {

        @Test
        @DisplayName("여행 생성 성공")
        void createTravel_Success() {
            // given
            String title = "새로운 여행";
            LocalDate startDate = today.plusDays(1);
            LocalDate endDate = today.plusDays(5);
            String expectedPresignedUrl = "https://s3.amazonaws.com/presigned-url";

            when(s3Interface.generatePresignedUrl(anyString(), any(S3FileType.class)))
                    .thenReturn(expectedPresignedUrl);

            // when
            String presignedUrl = travelService.createTravel(testMember, title, startDate, endDate, today);

            // then
            verify(travelRepository).save(any(Travel.class));
            verify(s3Interface).generatePresignedUrl(anyString(), eq(S3FileType.BANNER));
            assertThat(presignedUrl).isEqualTo(expectedPresignedUrl);
        }
    }

    @Nested
    @DisplayName("여행 조회 테스트")
    class GetTravelsTest {

        @Test
        @DisplayName("회원의 모든 여행 조회 성공")
        void getTravels_Success() {
            // given
            Traveler traveler = TravelFixture.createTraveler(testTravel, testMember);
            
            testMember.addTraveler(traveler);
            String expectedPresignedUrl = "https://s3.amazonaws.com/presigned-url";

            when(s3Interface.generatePresignedUrl(anyString(), any(S3FileType.class)))
                    .thenReturn(expectedPresignedUrl);

            // when
            List<Travel> travels = travelService.getTravels(testMember, today);

            // then
            assertThat(travels).hasSize(1);
            assertThat(travels.get(0)).isEqualTo(testTravel);
            assertThat(travels.get(0).getImageUrl()).isEqualTo(expectedPresignedUrl);
        }
    }

    @Nested
    @DisplayName("여행 삭제 테스트")
    class DeleteTravelTest {

        @Test
        @DisplayName("여행 삭제 성공 - 소유자인 경우")
        void deleteTravel_Success_Owner() {
            // given
            when(travelRepository.findById(any(Long.class))).thenReturn(Optional.of(testTravel));

            // when
            Long deletedTravelId = travelService.deleteTravel(testMember, 1L);

            // then
            assertThat(deletedTravelId).isEqualTo(1L);
            verify(travelRepository).delete(testTravel);
            verify(s3Service).deleteFile(testTravel.getImageUrl());
        }

        @Test
        @DisplayName("여행 삭제 실패 - 소유자가 아닌 경우")
        void deleteTravel_Fail_NotOwner() {
            // given
            Member otherMember = MemberFixture.createMemberWithId(2L);
            when(travelRepository.findById(any(Long.class))).thenReturn(Optional.of(testTravel));

            // when & then
            assertThatThrownBy(() -> travelService.deleteTravel(otherMember, 1L))
                .isInstanceOf(CommonErrorHandler.class)
                .extracting(ex -> ((CommonErrorHandler) ex).getCode())
                .isEqualTo(ErrorStatus.UNAUTHORIZED_ACCESS_TO_TRAVEL);
            verify(travelRepository, never()).delete(any(Travel.class));
        }

        @Test
        @DisplayName("여행 삭제 실패 - 여행이 존재하지 않는 경우")
        void deleteTravel_Fail_TravelNotFound() {
            // given
            when(travelRepository.findById(any(Long.class))).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> travelService.deleteTravel(testMember, 1L))
                .isInstanceOf(CommonErrorHandler.class)
                .extracting(ex -> ((CommonErrorHandler) ex).getCode())
                .isEqualTo(ErrorStatus.TRAVEL_LOG_NOT_FOUND);
            
            verify(travelRepository, never()).delete(any(Travel.class));
        }
    }

    @Nested
    @DisplayName("여행 수정 테스트")
    class EditTravelTest {

        @Test
        @DisplayName("여행 수정 성공")
        void editTravel_Success() {
            // given
            String newTitle = "수정된 여행";
            LocalDate newStartDate = today.plusDays(2);
            LocalDate newEndDate = today.plusDays(6);
            String expectedPresignedUrl = "https://s3.amazonaws.com/presigned-url";

            // 테스트 멤버가 여행자인 상황 설정
            Traveler traveler = TravelFixture.createTraveler(testTravel, testMember);
            testTravel.addTravelMember(traveler);

            when(travelRepository.findById(any(Long.class))).thenReturn(Optional.of(testTravel));
            when(s3Interface.generatePresignedUrl(anyString(), any(S3FileType.class)))
                    .thenReturn(expectedPresignedUrl);

            // when
            String presignedUrl = travelService.editTravel(testMember, 1L, newTitle, newStartDate, newEndDate, today);

            // then
            assertThat(presignedUrl).isEqualTo(expectedPresignedUrl);
            assertThat(testTravel.getTitle()).isEqualTo(newTitle);
            assertThat(testTravel.getStartDate()).isEqualTo(newStartDate);
            assertThat(testTravel.getEndDate()).isEqualTo(newEndDate);
        }

        @Test
        @DisplayName("여행 수정 실패 - 여행자가 아닌 경우")
        void editTravel_Fail_NotTraveler() {
            // given
            Member otherMember = MemberFixture.createMemberWithId(2L);
            when(travelRepository.findById(any(Long.class))).thenReturn(Optional.of(testTravel));

            // when & then
            assertThatThrownBy(() -> travelService.editTravel(otherMember, 1L, "수정 여행", today, today.plusDays(5), today))
                .isInstanceOf(CommonErrorHandler.class)
                .extracting(ex -> ((CommonErrorHandler) ex).getCode())
                .isEqualTo(ErrorStatus.UNAUTHORIZED_ACCESS_TO_TRAVEL);
        }
    }

    @Nested
    @DisplayName("초대 코드 테스트")
    class InvitationCodeTest {

        @Test
        @DisplayName("초대 코드 생성 성공")
        void getInvitationCode_Success_Create() {
            // given
            // 테스트 멤버가 여행자인 상황 설정
            Traveler traveler = TravelFixture.createTraveler(testTravel, testMember);
            testTravel.addTravelMember(traveler);

            when(travelRepository.findById(any(Long.class))).thenReturn(Optional.of(testTravel));
            when(travelRepository.findByInvitationCode(anyString())).thenReturn(Optional.empty());

            // when
            String invitationCode = travelService.getInvitationCode(testMember, 1L);

            // then
            assertThat(invitationCode).isNotNull();
            assertThat(testTravel.getInvitationCode()).isEqualTo(invitationCode);
        }

        @Test
        @DisplayName("이미 존재하는 초대 코드 반환")
        void getInvitationCode_Success_Existing() {
            // given
            String existingCode = "EXISTING_CODE";
            testTravel.setInvitationCode(existingCode);
            
            // 테스트 멤버가 여행자인 상황 설정
            Traveler traveler = TravelFixture.createTraveler(testTravel, testMember);
            testTravel.addTravelMember(traveler);

            when(travelRepository.findById(any(Long.class))).thenReturn(Optional.of(testTravel));

            // when
            String invitationCode = travelService.getInvitationCode(testMember, 1L);

            // then
            assertThat(invitationCode).isEqualTo(existingCode);
        }
    }

    @Nested
    @DisplayName("여행 참여 테스트")
    class JoinTravelTest {

        @Test
        @DisplayName("여행 참여 성공")
        void join_Success() {
            // given
            String invitationCode = "INVITE_CODE";
            Member newMember = MemberFixture.createMemberWithId(2L);

            when(travelRepository.findByInvitationCode(invitationCode)).thenReturn(Optional.of(testTravel));

            // when
            Traveler traveler = travelService.join(newMember, invitationCode);

            // then
            assertThat(traveler).isNotNull();
            assertThat(traveler.getTravel()).isEqualTo(testTravel);
            assertThat(traveler.getMember()).isEqualTo(newMember);
            assertThat(testTravel.isTraveler(newMember)).isTrue();
        }

        @Test
        @DisplayName("여행 참여 실패 - 잘못된 초대 코드")
        void join_Fail_InvalidCode() {
            // given
            String invalidCode = "INVALID_CODE";
            when(travelRepository.findByInvitationCode(invalidCode)).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> travelService.join(testMember, invalidCode))
                .isInstanceOf(CommonErrorHandler.class)
                .extracting(ex -> ((CommonErrorHandler) ex).getCode())
                .isEqualTo(ErrorStatus.INVITATION_CODE_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("여행 멤버 테스트")
    class TravelMembersTest {

        @Test
        @DisplayName("여행 멤버 조회 성공")
        void getMembers_Success() {
            // given
            Member member2 = MemberFixture.createMemberWithId(2L);
            
            // 테스트 멤버가 여행자인 상황 설정
            Traveler traveler1 = TravelFixture.createTraveler(testTravel, testMember);
            Traveler traveler2 = TravelFixture.createTraveler(testTravel, member2);
            
            testTravel.addTravelMember(traveler1);
            testTravel.addTravelMember(traveler2);

            when(travelRepository.findById(any(Long.class))).thenReturn(Optional.of(testTravel));

            // when
            List<Member> members = travelService.getMembers(testMember, 1L);

            // then
            assertThat(members).hasSize(2);
            assertThat(members).contains(testMember, member2);
        }

        @Test
        @DisplayName("여행 멤버 조회 실패 - 여행자가 아닌 경우")
        void getMembers_Fail_NotTraveler() {
            // given
            Member otherMember = MemberFixture.createMemberWithId(2L);
            when(travelRepository.findById(any(Long.class))).thenReturn(Optional.of(testTravel));

            // when & then
            assertThatThrownBy(() -> travelService.getMembers(otherMember, 1L))
                .isInstanceOf(CommonErrorHandler.class)
                .extracting(ex -> ((CommonErrorHandler) ex).getCode())
                .isEqualTo(ErrorStatus.UNAUTHORIZED_ACCESS_TO_TRAVEL);
        }

        @Test
        @DisplayName("여행 탈퇴 성공")
        void leave_Success() {
            // given
            Member memberToLeave = MemberFixture.createMemberWithId(2L);
            
            // 테스트 멤버와 탈퇴할 멤버가 여행자인 상황 설정
            Traveler traveler1 = TravelFixture.createTraveler(testTravel, testMember);
            Traveler traveler2 = TravelFixture.createTraveler(testTravel, memberToLeave);
            
            testTravel.addTravelMember(traveler1);
            testTravel.addTravelMember(traveler2);

            when(travelRepository.findById(any(Long.class))).thenReturn(Optional.of(testTravel));
            when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(memberToLeave));

            // when
            travelService.leave(testMember, 1L, 2L);

            // then
            assertThat(testTravel.isTraveler(memberToLeave)).isFalse();
            assertThat(testTravel.isTraveler(testMember)).isTrue();
        }
    }
}
