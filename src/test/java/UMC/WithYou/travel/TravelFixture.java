package UMC.WithYou.travel;

import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.travel.domain.Travel;
import UMC.WithYou.feature.travel.domain.TravelStatus;
import UMC.WithYou.feature.travel.domain.Traveler;
import UMC.WithYou.member.MemberFixture;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Travel 및 Traveler 객체를 테스트에서 쉽게 생성하기 위한 Fixture 클래스
 */
public class TravelFixture {
    
    /**
     * 기본 Travel 객체를 생성합니다.
     * @return 기본값으로 설정된 Travel 객체
     */
    public static Travel createDefaultTravel() {
        Member owner = MemberFixture.createDefaultMember();
        return new Travel(
                owner,
                "테스트 여행",
                LocalDate.now().plusDays(1), // 내일 출발
                LocalDate.now().plusDays(3)  // 3일 후 종료
        );
    }
    
    /**
     * ID가 설정된 Travel 객체를 생성합니다.
     * @param id 설정할 ID 값
     * @return ID가 설정된 Travel 객체
     */
    public static Travel createTravelWithId(Long id) {
        Travel travel = createDefaultTravel();
        setTravelId(travel, id);
        return travel;
    }
    
    /**
     * 사용자 정의 정보를 가진 Travel 객체를 생성합니다.
     * @param owner 여행 소유자
     * @param title 여행 제목
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 사용자 정의 정보로 설정된 Travel 객체
     */
    public static Travel createCustomTravel(Member owner, String title, LocalDate startDate, LocalDate endDate) {
        return new Travel(owner, title, startDate, endDate);
    }
    
    /**
     * 사용자 정의 정보와 ID를 가진 Travel 객체를 생성합니다.
     * @param id 설정할 ID 값
     * @param owner 여행 소유자
     * @param title 여행 제목
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 사용자 정의 정보와 ID로 설정된 Travel 객체
     */
    public static Travel createCustomTravelWithId(Long id, Member owner, String title, LocalDate startDate, LocalDate endDate) {
        Travel travel = createCustomTravel(owner, title, startDate, endDate);
        setTravelId(travel, id);
        return travel;
    }
    
    /**
     * 특정 상태를 가진 Travel 객체를 생성합니다.
     * @param status 설정할 여행 상태
     * @return 특정 상태가 설정된 Travel 객체
     */
    public static Travel createTravelWithStatus(TravelStatus status) {
        Travel travel = createDefaultTravel();
        LocalDate today = LocalDate.now();
        
        if (status == TravelStatus.BEFORE) {
            travel = new Travel(
                MemberFixture.createDefaultMember(),
                "예정된 여행",
                today.plusDays(5),
                today.plusDays(10)
            );
        } else if (status == TravelStatus.ONGOING) {
            travel = new Travel(
                MemberFixture.createDefaultMember(),
                "진행 중인 여행",
                today.minusDays(2),
                today.plusDays(3)
            );
        } else if (status == TravelStatus.AFTER) {
            travel = new Travel(
                MemberFixture.createDefaultMember(),
                "지난 여행",
                today.minusDays(10),
                today.minusDays(5)
            );
        }
        
        travel.setTravelStatus(today);
        return travel;
    }
    
    /**
     * 초대 코드가 설정된 Travel 객체를 생성합니다.
     * @return 초대 코드가 설정된 Travel 객체
     */
    public static Travel createTravelWithInvitationCode() {
        Travel travel = createDefaultTravel();
        travel.setInvitationCode(UUID.randomUUID().toString().substring(0, 6));
        return travel;
    }
    
    /**
     * 이미지 URL이 설정된 Travel 객체를 생성합니다.
     * @param imageUrl 이미지 URL
     * @return 이미지 URL이 설정된 Travel 객체
     */
    public static Travel createTravelWithImage(String imageUrl) {
        Travel travel = createDefaultTravel();
        travel.setImageUrl(imageUrl);
        return travel;
    }
    
    /**
     * 여행 멤버가 추가된 Travel 객체를 생성합니다.
     * @param members 추가할 Member 객체 목록
     * @return 여행 멤버가 추가된 Travel 객체
     */
    public static Travel createTravelWithMembers(List<Member> members) {
        Travel travel = createDefaultTravel();
        
        for (Member member : members) {
            Traveler traveler = new Traveler(travel, member);
            travel.addTravelMember(traveler);
            member.addTraveler(traveler);
        }
        
        return travel;
    }
    
    /**
     * 특정 수의 여행자가 있는 Travel 객체를 생성합니다.
     * @param memberCount 생성할 여행자 수
     * @return 여행자가 추가된 Travel 객체
     */
    public static Travel createTravelWithMemberCount(int memberCount) {
        Travel travel = createDefaultTravel();
        Member owner = travel.getMember();
        Traveler ownerTraveler = new Traveler(travel, owner);
        travel.addTravelMember(ownerTraveler);
        
        for (int i = 0; i < memberCount - 1; i++) {
            Member member = MemberFixture.createMemberWithId((long) (i + 2));
            Traveler traveler = new Traveler(travel, member);
            travel.addTravelMember(traveler);
            member.addTraveler(traveler);
        }
        
        return travel;
    }
    
    /**
     * Travel 객체의 ID를 설정합니다 (리플렉션 사용).
     * @param travel ID를 설정할 Travel 객체
     * @param id 설정할 ID 값
     */
    public static void setTravelId(Travel travel, Long id) {
        try {
            Field idField = Travel.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(travel, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Travel ID 설정 중 에러 발생: " + e.getMessage(), e);
        }
    }
    
    /**
     * Traveler 객체를 생성합니다.
     * @param travel 여행 정보
     * @param member 회원 정보
     * @return 생성된 Traveler 객체
     */
    public static Traveler createTraveler(Travel travel, Member member) {
        return new Traveler(travel, member);
    }
    
    /**
     * ID가 설정된 Traveler 객체를 생성합니다.
     * @param travel 여행 정보
     * @param member 회원 정보
     * @param id 설정할 ID 값
     * @return ID가 설정된 Traveler 객체
     */
    public static Traveler createTravelerWithId(Travel travel, Member member, Long id) {
        Traveler traveler = createTraveler(travel, member);
        setTravelerId(traveler, id);
        return traveler;
    }
    
    /**
     * Traveler 객체의 ID를 설정합니다 (리플렉션 사용).
     * @param traveler ID를 설정할 Traveler 객체
     * @param id 설정할 ID 값
     */
    public static void setTravelerId(Traveler traveler, Long id) {
        try {
            Field idField = Traveler.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(traveler, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Traveler ID 설정 중 에러 발생: " + e.getMessage(), e);
        }
    }
    
    /**
     * 서로 다른 ID를 가진 여러 Travel 객체를 생성합니다.
     * @param count 생성할 Travel 객체 수
     * @return 서로 다른 ID를 가진 Travel 객체 배열
     */
    public static Travel[] createMultipleTravelsWithIds(int count) {
        Travel[] travels = new Travel[count];
        for (int i = 0; i < count; i++) {
            travels[i] = createTravelWithId((long) (i + 1));
        }
        return travels;
    }
}