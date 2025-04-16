package UMC.WithYou.feature.notice.controller;

import UMC.WithYou.common.apiPayload.WithUResponse;
import UMC.WithYou.feature.notice.controller.dto.NoticeCheckResponseDTO;
import UMC.WithYou.feature.notice.controller.dto.NoticeCheckResponseDTO.ResultDto;
import UMC.WithYou.feature.notice.converter.NoticeCheckConverter;
import UMC.WithYou.feature.notice.domain.NoticeCheck;
import UMC.WithYou.feature.notice.service.NoticeCheckCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/noticeCheck")
public class NoticeCheckController {

    private final NoticeCheckCommandService noticeCheckCommandService;

    @Operation(summary="notice 체크 API")
    @PatchMapping
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "NOTICE2000",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "NOTICE4023", description = "해당 notice가 없습니다",content = @Content(schema = @Schema(implementation = WithUResponse.class))),
    })
    @Parameters({
            @Parameter(name = "noticeId", description = "notice 의 아이디, query param 입니다!"),
            @Parameter(name = "memberId", description = "notice 의 아이디, query param 입니다!"),
    })
    public WithUResponse<NoticeCheckResponseDTO.ResultDto> checkBox(@RequestParam("noticeId") Long noticeId, @RequestParam("memberId") Long memberId){
        NoticeCheck noticeCheck=noticeCheckCommandService.checkBox(noticeId,memberId);
        return WithUResponse.onSuccess(NoticeCheckConverter.toResultDTO(noticeCheck));
    }
}
