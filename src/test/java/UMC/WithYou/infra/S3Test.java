package UMC.WithYou.infra;

import UMC.WithYou.infra.s3.S3FileType;
import UMC.WithYou.infra.s3.S3PreSignService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class S3Test {

    @Autowired
    private S3PreSignService s3Service;

    @Test
    @DisplayName("성공적으로 presigned URL을 받아옴")
    void generatePresignedUrlSuccessTest() {
            // given
            String userId = "testUserId";

            // when
            String presignedUrl = s3Service.generatePresignedUrl(userId, S3FileType.PROFILE);
            
            // then
            assertThat(presignedUrl).isNotNull();
            assertThat(presignedUrl).contains(userId);
            assertThat(presignedUrl).contains(".amazonaws.com");
            System.out.println(presignedUrl);
            // URL 형식으로 파싱이 가능한지 검증
            try {
                URL url = new URL(presignedUrl);
                assertThat(url.getProtocol()).isEqualTo("https");
                assertThat(url.getPath()).contains(userId + "/profile.png");
            } catch (Exception e) {
            throw new AssertionError("생성된 URL이 유효한 형식이 아닙니다: " + presignedUrl, e);
        }
    }
}