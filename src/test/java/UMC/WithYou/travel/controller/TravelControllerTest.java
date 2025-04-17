package UMC.WithYou.travel.controller;

import UMC.WithYou.common.apiPayload.code.status.SuccessStatus;
import UMC.WithYou.common.config.TestWebMvcConfig;
import UMC.WithYou.common.security.WithMockCustomUser;
import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.travel.controller.TravelController;
import UMC.WithYou.feature.travel.domain.Travel;
import UMC.WithYou.feature.travel.domain.Traveler;
import UMC.WithYou.feature.travel.service.TravelService;
import UMC.WithYou.member.MemberFixture;
import UMC.WithYou.travel.TravelFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TravelController.class)
@ContextConfiguration(classes = {TravelController.class, TestWebMvcConfig.class})
class TravelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TravelService travelService;

    private final LocalDate testDate = LocalDate.now();

    @Test
    @DisplayName("여행 생성 테스트")
    @WithMockCustomUser(name = "테스트유저", id = 1L)
    void createTravelTest() throws Exception {
        // given
        String testTitle = "테스트 여행";
        LocalDate startDate = testDate;
        LocalDate endDate = testDate.plusDays(7);
        
        // DTO 객체 생성 - 직접 JSON 문자열 생성
        String requestDTOJson = String.format(
            "{\"title\":\"%s\",\"content\":\"\",\"localDate\":\"%s\",\"startDate\":\"%s\",\"endDate\":\"%s\"}",
            testTitle, testDate, startDate, endDate);
        
        String expectedUrl = "https://s3.amazonaws.com/travel-banner/123.jpg";
        when(travelService.createTravel(any(Member.class), eq(testTitle), eq(startDate), eq(endDate), eq(testDate)))
                .thenReturn(expectedUrl);
        
        // when & then
        MockMultipartFile requestPart = new MockMultipartFile(
                "request", "", "application/json", 
                requestDTOJson.getBytes());
        
        mockMvc.perform(multipart("/api/v1/travels")
                .file(requestPart))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SuccessStatus._OK.getCode()))
                .andExpect(jsonPath("$.message").value(SuccessStatus._OK.getMessage()))
                .andExpect(jsonPath("$.result.url").value(expectedUrl));
    }

    @Test
    @DisplayName("여행 목록 조회 테스트")
    @WithMockCustomUser(name = "테스트유저", id = 1L)
    void getTravelThumbnailsTest() throws Exception {
        // given
        Member testMember = MemberFixture.createMemberWithId(1L);
        Travel travel1 = TravelFixture.createCustomTravelWithId(1L, testMember, "여행 1", testDate, testDate.plusDays(5));
        Travel travel2 = TravelFixture.createCustomTravelWithId(2L, testMember, "여행 2", testDate.plusDays(10), testDate.plusDays(15));
        
        travel1.setImageUrl("https://s3.amazonaws.com/travel1.jpg");
        travel2.setImageUrl("https://s3.amazonaws.com/travel2.jpg");
        
        List<Travel> travelList = Arrays.asList(travel1, travel2);
        when(travelService.getTravels(any(Member.class), any(LocalDate.class))).thenReturn(travelList);
        
        // when & then
        mockMvc.perform(get("/api/v1/travels")
                .param("localDate", testDate.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SuccessStatus._OK.getCode()))
                .andExpect(jsonPath("$.message").value(SuccessStatus._OK.getMessage()))
                .andExpect(jsonPath("$.result.length()").value(2))
                .andExpect(jsonPath("$.result[0].travelId").value(1))
                .andExpect(jsonPath("$.result[0].title").value("여행 1"))
                .andExpect(jsonPath("$.result[1].travelId").value(2))
                .andExpect(jsonPath("$.result[1].title").value("여행 2"));
    }

    @Test
    @DisplayName("여행 삭제 테스트")
    @WithMockCustomUser(name = "테스트유저", id = 1L)
    void deleteTravelTest() throws Exception {
        // given
        Long travelId = 1L;
        when(travelService.deleteTravel(any(Member.class), eq(travelId))).thenReturn(travelId);
        
        // when & then
        mockMvc.perform(delete("/api/v1/travels/{travelId}", travelId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SuccessStatus._OK.getCode()))
                .andExpect(jsonPath("$.message").value(SuccessStatus._OK.getMessage()))
                .andExpect(jsonPath("$.result.travelId").value(travelId));
    }

    @Test
    @DisplayName("여행 수정 테스트")
    @WithMockCustomUser(name = "테스트유저", id = 1L)
    void editTravelTest() throws Exception {
        // given
        Long travelId = 1L;
        String newTitle = "수정된 여행";
        LocalDate newStartDate = testDate.plusDays(1);
        LocalDate newEndDate = testDate.plusDays(8);
        
        // DTO 객체 생성 - 직접 JSON 문자열 생성
        String requestDTOJson = String.format(
            "{\"title\":\"%s\",\"startDate\":\"%s\",\"endDate\":\"%s\",\"localDate\":\"%s\"}",
            newTitle, newStartDate, newEndDate, testDate);
        
        String expectedUrl = "https://s3.amazonaws.com/travel-banner/123-updated.jpg";
        when(travelService.editTravelWithImage(any(Member.class), eq(travelId), eq(newTitle), eq(newStartDate), eq(newEndDate), eq(testDate)))
                .thenReturn(expectedUrl);
        
        // when & then
        MockMultipartFile requestPart = new MockMultipartFile(
                "request", "", "application/json", 
                requestDTOJson.getBytes());
        
        mockMvc.perform(multipart("/api/v1/travels/{travelId}", travelId)
                .file(requestPart)
                .with(request -> {
                    request.setMethod("PATCH");
                    return request;
                }))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SuccessStatus._OK.getCode()))
                .andExpect(jsonPath("$.message").value(SuccessStatus._OK.getMessage()))
                .andExpect(jsonPath("$.result.url").value(expectedUrl));
    }

    @Test
    @DisplayName("여행 멤버 조회 테스트")
    @WithMockCustomUser(name = "테스트유저", id = 1L)
    void getTravelMembersTest() throws Exception {
        // given
        Long travelId = 1L;
        Member member1 = MemberFixture.createMemberWithId(1L);
        Member member2 = MemberFixture.createMemberWithId(2L);
        member2.updateName("두번째 사용자");
        
        List<Member> memberList = Arrays.asList(member1, member2);
        when(travelService.getMembers(any(Member.class), eq(travelId))).thenReturn(memberList);
        
        // when & then
        mockMvc.perform(get("/api/v1/travels/{travelId}/members", travelId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SuccessStatus._OK.getCode()))
                .andExpect(jsonPath("$.message").value(SuccessStatus._OK.getMessage()))
                .andExpect(jsonPath("$.result.length()").value(2))
                .andExpect(jsonPath("$.result[0].memberId").value(1))
                .andExpect(jsonPath("$.result[1].memberId").value(2))
                .andExpect(jsonPath("$.result[1].name").value("두번째 사용자"));
    }

    @Test
    @DisplayName("초대 코드 조회 테스트")
    @WithMockCustomUser(name = "테스트유저", id = 1L)
    void getInvitationCodeTest() throws Exception {
        // given
        Long travelId = 1L;
        String invitationCode = "ABCDEF123456";
        when(travelService.getInvitationCode(any(Member.class), eq(travelId))).thenReturn(invitationCode);
        
        // when & then
        mockMvc.perform(get("/api/v1/travels/{travelId}/invitation_code", travelId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SuccessStatus._OK.getCode()))
                .andExpect(jsonPath("$.message").value(SuccessStatus._OK.getMessage()))
                .andExpect(jsonPath("$.result.travelId").value(travelId))
                .andExpect(jsonPath("$.result.invitationCode").value(invitationCode));
    }

    @Test
    @DisplayName("여행 참여 테스트")
    @WithMockCustomUser(name = "테스트유저", id = 1L)
    void joinTravelTest() throws Exception {
        // given
        String invitationCode = "ABCDEF123456";
        String requestJson = String.format("{\"invitationCode\":\"%s\"}", invitationCode);
        
        Member testMember = MemberFixture.createMemberWithId(1L);
        Travel testTravel = TravelFixture.createCustomTravelWithId(1L, testMember, "테스트 여행", testDate, testDate.plusDays(5));
        Traveler traveler = TravelFixture.createTraveler(testTravel, testMember);
        
        when(travelService.join(any(Member.class), eq(invitationCode))).thenReturn(traveler);
        
        // when & then
        mockMvc.perform(patch("/api/v1/travels/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SuccessStatus._OK.getCode()))
                .andExpect(jsonPath("$.message").value(SuccessStatus._OK.getMessage()))
                .andExpect(jsonPath("$.result.memberId").value(1))
                .andExpect(jsonPath("$.result.travelId").value(1));
    }

    @Test
    @DisplayName("여행 탈퇴 테스트")
    @WithMockCustomUser(name = "테스트유저", id = 1L)
    void leaveTravelTest() throws Exception {
        // given
        Long travelId = 1L;
        Long memberId = 2L;
        
        doNothing().when(travelService).leave(any(Member.class), eq(travelId), eq(memberId));
        
        // when & then
        mockMvc.perform(patch("/api/v1/travels/{travelId}/members/{memberId}", travelId, memberId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SuccessStatus._OK.getCode()))
                .andExpect(jsonPath("$.message").value(SuccessStatus._OK.getMessage()))
                .andExpect(jsonPath("$.result.travelId").value(travelId))
                .andExpect(jsonPath("$.result.memberId").value(memberId));
    }
}
