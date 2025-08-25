package UMC.WithYou.auth.controller;

import UMC.WithYou.common.apiPayload.code.status.SuccessStatus;
import UMC.WithYou.common.config.TestWebMvcConfig;
import UMC.WithYou.feature.auth.controller.AuthController;
import UMC.WithYou.feature.auth.controller.LoginResponse;
import UMC.WithYou.feature.auth.controller.RefreshRequest;
import UMC.WithYou.feature.auth.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@ContextConfiguration(classes = {AuthController.class, TestWebMvcConfig.class})
class AuthControllerRefreshTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("/api/v1/auth/refresh 재발급 성공")
    void refresh_success() throws Exception {
        // given
        RefreshRequest req = new RefreshRequest();
        req.setRefreshToken("old-refresh");

        when(authService.refreshAccessToken(anyString()))
                .thenReturn(LoginResponse.builder().accessToken("new-access").refreshToken("new-refresh").build());

        // when & then
        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(SuccessStatus._OK.getCode()))
                .andExpect(jsonPath("$.message").value(SuccessStatus._OK.getMessage()))
                .andExpect(jsonPath("$.result.accessToken").value("new-access"))
                .andExpect(jsonPath("$.result.refreshToken").value("new-refresh"));
    }
}


