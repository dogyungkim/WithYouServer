package UMC.WithYou.feature.auth.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "accessToken is mandatory")
    @Schema(description = "Access token from the authentication provider", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", required = true)
    private String accessToken;

    @NotBlank(message = "provider is mandatory")
    @Schema(description = "Authentication provider", example = "google", required = true)
    private String provider;

    @Schema(description = "email address", example = "user@example.com", required = false)
    private String email;

    @Schema(description = "name", example = "장호진", required = false)
    private String name;

    @Schema(description = "Nonce for the authentication request", example = "c29tZS1yYW5kb20tc3RyaW5nLXdpdGgtZW50cm9weQ==", required = false)
    private String nonce;
}
