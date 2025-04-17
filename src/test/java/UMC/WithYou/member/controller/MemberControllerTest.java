package UMC.WithYou.member.controller;

import UMC.WithYou.common.apiPayload.code.status.SuccessStatus;
import UMC.WithYou.common.config.TestWebMvcConfig;
import UMC.WithYou.common.security.WithMockCustomUser;
import UMC.WithYou.feature.member.controller.MemberController;
import UMC.WithYou.feature.member.controller.NameRequest;
import UMC.WithYou.feature.member.controller.RegisterRequest;
import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberController.class)
@ContextConfiguration(classes = {MemberController.class, TestWebMvcConfig.class})
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @Test
    @DisplayName("회원 이름 변경 테스트")
    @WithMockCustomUser(name = "테스트유저", id = 1L)
    void updateNameTest() throws Exception {
        // given
        NameRequest nameRequest = new NameRequest();
        nameRequest.setName("새이름");
        
        // // when & then
        doNothing().when(memberService).updateName(any(Member.class), eq(nameRequest));
        
        mockMvc.perform(patch("/api/v1/member/name")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nameRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SuccessStatus._OK.getCode()))
                .andExpect(jsonPath("$.message").value(SuccessStatus._OK.getMessage()));
    }

    @Test
    @DisplayName("회원 정보 조회 테스트")
    @WithMockCustomUser(name = "테스트유저", id = 1L)
    void getMemberTest() throws Exception {
        // when & then
        mockMvc.perform(get("/api/v1/member"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SuccessStatus._OK.getCode()))
                .andExpect(jsonPath("$.message").value(SuccessStatus._OK.getMessage()))
                .andExpect(jsonPath("$.result.name").value("테스트유저"))
                .andExpect(jsonPath("$.result.profileImageKey").isEmpty());
    }

    @Test
    @DisplayName("회원 가입 테스트")
    @WithMockCustomUser
    void setMemberInfoTest() throws Exception {
        // given
        RegisterRequest registerRequest = new RegisterRequest("테스트유저");
        String presignedUrl = "https://s3.bucket.com/user-id/profile.png?X-Amz-Algorithm=...";
        
        // when
        when(memberService.setMemberInfo(any(Member.class), eq("테스트유저"))).thenReturn(presignedUrl);
        
        // then
        mockMvc.perform(post("/api/v1/member/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SuccessStatus._OK.getCode()))
                .andExpect(jsonPath("$.message").value(SuccessStatus._OK.getMessage()))
                .andExpect(jsonPath("$.result.name").value("테스트유저"))
                .andExpect(jsonPath("$.result.presignedUrl").value(presignedUrl));
    }
    
    @Test
    @DisplayName("이미지 업데이트 프리사인드 URL 발급 테스트")
    @WithMockCustomUser(name = "테스트유저", id = 1L)
    void getUpdateImageUrlTest() throws Exception {
        // given
        String expectedPresignedUrl = "https://s3.bucket.com/user-id/profile-update.png?X-Amz-Algorithm=...";
        
        // when
        when(memberService.getUpdateImageUrl(any(Member.class))).thenReturn(expectedPresignedUrl);
        
        // then
        mockMvc.perform(get("/api/v1/member/image"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SuccessStatus._OK.getCode()))
                .andExpect(jsonPath("$.message").value(SuccessStatus._OK.getMessage()))
                .andExpect(jsonPath("$.result").value(expectedPresignedUrl));
    }
}
