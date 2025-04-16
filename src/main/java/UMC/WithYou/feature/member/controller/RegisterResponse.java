package UMC.WithYou.feature.member.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {
    @Schema(description = "사용자 이름", example = "홍길동")
    private String name;
    
    @Schema(description = "프로필 이미지 업로드용 Presigned URL", example = "https://s3.bucket.com/user-id/profile.png?X-Amz-Algorithm=...")
    private String presignedUrl;
} 