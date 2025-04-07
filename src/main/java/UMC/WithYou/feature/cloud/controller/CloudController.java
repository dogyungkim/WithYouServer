package UMC.WithYou.feature.cloud.controller;

import UMC.WithYou.common.apiPayload.ApiResponse;
import UMC.WithYou.feature.cloud.converter.CloudConverter;
import UMC.WithYou.feature.cloud.domain.Cloud;
import UMC.WithYou.feature.cloud.service.CloudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/cloud")
public class CloudController {

    private final CloudService cloudService;

    @Operation(summary="cloud 생성 API")
    @PostMapping
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TRAVEL003", description = "해당 travel가 없습니다",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<CloudResponseDTO.ResultDto> create(
            @RequestPart List<MultipartFile> cloudImage,
            @RequestPart CloudRequestDTO.CloudJoinDto request
            ){
        Cloud cloud= cloudService.createCloud(request, cloudImage);
        return ApiResponse.onSuccess(CloudConverter.toResultDTO(cloud));
    }

    @Operation(summary="cloud 조회 API")
    @GetMapping("/{travelId}")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "NOTICE2000",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "TRAVEL4003", description = "해당 travel log가 없습니다",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "logId", description = "travel log 의 아이디, path variable 입니다!"),
    })
    public ApiResponse<List<CloudResponseDTO.PictureDto>> getDateNotice(@PathVariable Long travelId){
        List<CloudResponseDTO.PictureDto> pictures=cloudService.getPictures(travelId);
        return ApiResponse.onSuccess(pictures);
    }

    @Operation(summary = "cloud 삭제 API")
    @DeleteMapping
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "NOTICE2000",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CLOUD4003", description = "해당 cloud가 없습니다",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<CloudResponseDTO.ResultDto> deletePictures(@RequestBody @Valid CloudRequestDTO.DeleteDto request){
        Cloud cloud=cloudService.deletePictures(request.getCloudId(),request.getFiles());
        return ApiResponse.onSuccess(CloudConverter.toResultDTO(cloud));
    }

}
