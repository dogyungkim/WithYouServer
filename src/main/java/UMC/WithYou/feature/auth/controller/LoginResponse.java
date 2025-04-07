package UMC.WithYou.feature.auth.controller;

import lombok.Builder;
import lombok.Data;

@Data
public class LoginResponse {
    private String accessToken;

    private String refreshToken;

    @Builder
    public LoginResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
