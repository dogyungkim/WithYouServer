package UMC.WithYou.feature.packingItem.controller;

import UMC.WithYou.common.apiPayload.ApiResponse;
import UMC.WithYou.feature.packingItem.controller.PackingItemRequest.*;
import UMC.WithYou.feature.packingItem.controller.PackingItemResponse.*;
import UMC.WithYou.feature.packingItem.domain.PackingItem;
import UMC.WithYou.feature.packingItem.service.PackingItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PackingItemController {
    private final PackingItemService packingItemService;

    public PackingItemController(PackingItemService packingItemService){
        this.packingItemService = packingItemService;
    }

    @Operation(summary = "짐 목록 추가")
    @Parameters({
            @Parameter(name = "travelId", description = "path variable: 짐 목록을 추가할 여행 로그 아이디"),
    })
    @PostMapping("api/v1/travels/{travelId}/packing_items")
    public ApiResponse<AdditionResponseDTO> addPackingItem(
            @PathVariable @Valid Long travelId,
            @RequestBody @Valid AdditionRequestDTO additionRequestDTO){
        String itemName = additionRequestDTO.getItemName();
        PackingItem packingItem = packingItemService.addPackingItem(travelId, itemName);


        return ApiResponse.onSuccess(AdditionResponseDTO
                .builder()
                .packingItemId(packingItem.getId())
                .build());
    }

    @Operation(summary = "짐 목록 조회", description = "파라미터로 받은 여행 로그 ID에 해당하는 짐 목록을 모두 조회")
    @Parameters({
            @Parameter(name = "travelId", description = "path variable: 짐 목록을 조회할 여행 로그 아이디"),
    })
    @GetMapping("api/v1/travels/{travelId}/packing_items")
    public ApiResponse<List<SearchResponseDTO>> getPackingItems(@PathVariable Long travelId){
        List<PackingItem> packingItems = packingItemService.findPackingItems(travelId);

        return ApiResponse.onSuccess(packingItems.stream()
                .map(m -> new SearchResponseDTO(m))
                .toList());

    }


    @Operation(summary = "짐 목록 제거")
    @Parameters({
            @Parameter(name = "packingItemId", description = "path variable: 삭제할 짐 id"),
    })
    @DeleteMapping("api/v1/packing_items/{packingItemId}")
    public ApiResponse<DeletionResponseDTO> deletePackingItem(@PathVariable Long packingItemId){
        packingItemService.deletePackingItem(packingItemId);

        return ApiResponse.onSuccess(new DeletionResponseDTO(packingItemId));
    }



    @Operation(summary = "짐 목록 담당자 지정")
    @Parameters({
            @Parameter(name = "packingItemId", description = "path variable: 담당자를 지정할 짐 id"),
            @Parameter(name = "packer_id", description = "query string: 짐 담당 회원 id")
    })
    @PatchMapping("api/v1/packing_items/{packingItemId}/packer_choice")
    public ApiResponse<PackerChoiceResponseDTO> choosePacker(
        @PathVariable Long packingItemId, @RequestParam("packer_id") Long packerId)
    {
        Boolean checkboxState = packingItemService.setPacker(packingItemId, packerId);
        return ApiResponse.onSuccess(new PackerChoiceResponseDTO(packingItemId, packerId, checkboxState));
    }


    @Operation(summary = "짐 패킹 체크박스 토글")
    @Parameters({
            @Parameter(name = "packingItemId", description = "path variable: 토글할 짐 id"),
    })
    @PatchMapping("api/v1/packing_items/{packingItemId}")
    public ApiResponse<ToggleResponseDTO> toggleCheckbox(@PathVariable Long packingItemId){
        Boolean checkboxState = packingItemService.toggleCheckbox(packingItemId);
        return ApiResponse.onSuccess(new ToggleResponseDTO(packingItemId, checkboxState));
    }

}

