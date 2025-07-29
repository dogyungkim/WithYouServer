package UMC.WithYou.feature.member.controller;

import UMC.WithYou.common.annotation.AuthorizedMember;
import UMC.WithYou.common.apiPayload.WithUResponse;
import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "회원 이름 변경")
    @ApiResponse(responseCode = "200", description = "이름 업데이트 성공")
    @ApiResponse(responseCode = "400", description = "이름 업데이트 실패", content = @Content(schema = @Schema(implementation = String.class)))
    @PatchMapping("/name")
    public WithUResponse<Void> updateName(@AuthorizedMember Member member, @RequestBody NameRequest request) {
        memberService.updateName(member,request);
        return WithUResponse.onSuccess_NoContent();
    }

    /*
     * Deprecated : Use memberService.getUpdateImageUrl
     */
    // @Operation(summary = "이미지 변경")
    // @ApiResponse(responseCode = "200", description = "이미지 업데이트 성공")
    // @ApiResponse(responseCode = "400", description = "이미지 업데이트 실패", content = @Content(schema = @Schema(implementation = String.class)))
    // @PatchMapping("/image")
    // public WithUResponse<Void> updateImage(@AuthorizedMember Member member,
    //                         @RequestParam("image") @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "업데이트할 이미지 파일", required = true,
    //                                 content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "string", format = "binary"))) MultipartFile imageFile) {
    //     memberService.updateImage(member, imageFile);
    //     return WithUResponse.onSuccess_NoContent();
    // }

    @Operation(summary = "이미지 업데이트 프리사인드 URL 발급")
    @ApiResponse(responseCode = "200", description = "이미지 업데이트 프리사인드 URL 발급 성공")
    @ApiResponse(responseCode = "400", description = "이미지 업데이트 프리사인드 URL 발급 실패", content = @Content(schema = @Schema(implementation = String.class)))
    @GetMapping("/image")
    public WithUResponse<String> getUpdateImageUrl(@AuthorizedMember Member member) {
        String presignedUrl = memberService.getUpdateImageUrl(member);
        return WithUResponse.onSuccess(presignedUrl);
    }

    @Operation(summary = "회원 정보 조회")
    @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공")
    @ApiResponse(responseCode = "400", description = "회원 정보 조회 실패", content = @Content(schema = @Schema(implementation = String.class)))
    @GetMapping
    public WithUResponse<MemberResponse> getMember(@AuthorizedMember Member member) {
        MemberResponse memberResponse = MemberResponse.builder()
                .profileImageKey(member.getProfileImageKey())
                .name(member.getName())
                .build();
        return WithUResponse.onSuccess(memberResponse);
    }


    //OAuth로 회원가입 후 멤버 정보를 설정하는 Endpoint
    @Operation(summary = "회원 가입")
    @ApiResponse(responseCode = "200", description = "회원 가입 성공")
    @ApiResponse(responseCode = "400", description = "회원 가입 실패", content = @Content(schema = @Schema(implementation = String.class)))
    @PostMapping("/register")
    public WithUResponse<RegisterResponse> setMemberInfo(@AuthorizedMember Member member, @Valid @RequestBody RegisterRequest request) {
        String presignedUrl = memberService.setMemberInfo(member, request.name());
        
        return WithUResponse.onSuccess(
            RegisterResponse.builder()
                .presignedUrl(presignedUrl)
                .name(request.name())
                .build()
        );
    }
}
