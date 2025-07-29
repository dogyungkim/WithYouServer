package UMC.WithYou.member.service;

import UMC.WithYou.common.apiPayload.code.status.ErrorStatus;
import UMC.WithYou.common.apiPayload.exception.GeneralException;
import UMC.WithYou.feature.member.controller.NameRequest;
import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.member.domain.MemberType;
import UMC.WithYou.feature.member.service.MemberService;
import UMC.WithYou.infra.s3.S3FileType;
import UMC.WithYou.infra.s3.S3PreSignService;
import UMC.WithYou.infra.s3.S3Service;
import UMC.WithYou.member.MemberFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private S3Service s3Service;

    @Mock
    private S3PreSignService s3Interface;

    @InjectMocks
    private MemberService memberService;

    private Member memberFixture;

    @BeforeEach
    void setUp() {
        memberFixture = MemberFixture.createCustomMemberWithId(
            1L,
            "test@example.com",
            "test-identifier",
            "테스트회원",
            MemberType.BASIC_USER
        );
    }

    @Test
    @DisplayName("회원 이미지 업데이트 성공")
    void updateImage_Success() {
        // given
        MultipartFile imageFile = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );
        String expectedImageUrl = "https://s3.amazonaws.com/test-image.jpg";
        when(s3Service.uploadImg(any(MultipartFile.class))).thenReturn(expectedImageUrl);

        // when
        memberService.updateImage(memberFixture, imageFile);

        // then
        verify(s3Service).uploadImg(imageFile);
        assertEquals(expectedImageUrl, memberFixture.getProfileImageKey());
    }

    @Test
    @DisplayName("회원 이미지 업데이트 성공 - s3PreSignService 호출")
    void updateImage_Success_s3PreSignService_Call() {
        // given
        String expectedPresignedUrl = "https://s3.amazonaws.com/presigned-url";
        when(s3Interface.generatePresignedUrl(anyString(), any(S3FileType.class)))
                .thenReturn(expectedPresignedUrl);

        // when
        String url =memberService.getUpdateImageUrl(memberFixture);

        // then
        verify(s3Interface).generatePresignedUrl(memberFixture.getId().toString(), S3FileType.PROFILE);
        assertThat(url).isEqualTo(expectedPresignedUrl);
    }
    

    @Test
    @DisplayName("회원 이름 업데이트 성공")
    void updateName_Success() {
        // given
        String newName = "새로운이름";
        NameRequest request = new NameRequest();
        request.setName(newName);

        // when
        memberService.updateName(memberFixture, request);

        // then
        assertEquals(newName, memberFixture.getName());
    }

    @Test
    @DisplayName("회원 등록 성공")
    void setMemberInfo_Success() {
        // given
        String name = "새로운회원";
        String expectedPresignedUrl = "https://s3.amazonaws.com/presigned-url";
        when(s3Interface.generatePresignedUrl(anyString(), any(S3FileType.class)))
                .thenReturn(expectedPresignedUrl);

        // when
        String presignedUrl = memberService.setMemberInfo(memberFixture, name);

        // then
        verify(s3Interface).generatePresignedUrl(memberFixture.getId().toString(), S3FileType.PROFILE);
        assertEquals(name, memberFixture.getName());
        assertEquals(expectedPresignedUrl, memberFixture.getProfileImageKey());
        assertEquals(expectedPresignedUrl, presignedUrl);
    }

    @Test
    @DisplayName("회원 등록 실패 - 이름이 비어있는 경우")
    void setMemberInfo_Fail_EmptyName() {
        // given
        String emptyName = "";

        // when & then
        GeneralException exception = assertThrows(GeneralException.class,
                () -> memberService.setMemberInfo(memberFixture, emptyName));
        
        assertTrue(exception.getErrorReasonHttpStatus().getCode().equals(ErrorStatus._NOT_FOUND.getReasonHttpStatus().getCode()));
    }
}
